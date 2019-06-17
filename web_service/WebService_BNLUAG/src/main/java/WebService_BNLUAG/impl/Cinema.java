package WebService_BNLUAG.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import javax.jws.WebService;

import seatreservation.ArrayOfSeat;
import seatreservation.CinemaException;
import seatreservation.ICinema;
import seatreservation.ICinemaBuyCinemaException;
import seatreservation.ICinemaGetAllSeatsCinemaException;
import seatreservation.ICinemaGetSeatStatusCinemaException;
import seatreservation.ICinemaInitCinemaException;
import seatreservation.ICinemaLockCinemaException;
import seatreservation.ICinemaReserveCinemaException;
import seatreservation.ICinemaUnlockCinemaException;
import seatreservation.Lock;
import seatreservation.Seat;
import seatreservation.SeatStatus;

@WebService(
	name="CinemaService",
	portName="ICinema_HttpSoap11_Port",
	targetNamespace="http://www.iit.bme.hu/soi/hw/SeatReservation",
	endpointInterface="seatreservation.ICinema",
	wsdlLocation="WEB-INF/wsdl/SeatReservation.wsdl")

public class Cinema implements ICinema{
	public ArrayOfSeat seats = new ArrayOfSeat();
	public HashMap<String, SeatStatus> seat_statuses = new HashMap<String, SeatStatus>();
	public HashMap<String, Lock> seat_locks = new HashMap<String, Lock>();

	@Override
	public void init(int rows, int columns) throws ICinemaInitCinemaException {
		seats.getSeat().clear();
		seat_statuses.clear();
		seat_locks.clear();
		
		if (rows < 1 || rows > 26 || columns < 1 || columns > 100) {
			CinemaException cinema_exc = new CinemaException();
			cinema_exc.setErrorCode(1);
			cinema_exc.setErrorMessage("Row or column values out of boundary.");
			throw new ICinemaInitCinemaException("Row or column values out of boundary.", cinema_exc);
		}
		
		for (int i=1; i <= rows; i++) {
			for (int j=1; j<=columns; j++) {
				Seat seat = new Seat();
				seat.setRow(String.valueOf((char)(i + 64)));
				seat.setColumn(String.valueOf(j));
				
				seat_statuses.put(seat.getRow() + seat.getColumn(), SeatStatus.FREE);
				seats.getSeat().add(seat);
			}
		}
	}

	@Override
	public ArrayOfSeat getAllSeats() throws ICinemaGetAllSeatsCinemaException {
		return seats;
	}

	@Override
	public SeatStatus getSeatStatus(Seat seat) throws ICinemaGetSeatStatusCinemaException {
		if (seat_statuses.containsKey(seat.getRow() + seat.getColumn())) {
			return seat_statuses.get(seat.getRow() + seat.getColumn());
		}
		else {
			CinemaException cinema_exc = new CinemaException();
			cinema_exc.setErrorCode(3);
			cinema_exc.setErrorMessage("The seat is not valid.");
			throw new ICinemaGetSeatStatusCinemaException("The seat is not valid.", cinema_exc);
		}
	}

	@Override
	public String lock(Seat seat, int count) throws ICinemaLockCinemaException {
		boolean fault = false;
		CinemaException cinema_exc = new CinemaException();
		cinema_exc.setErrorCode(4);
		cinema_exc.setErrorMessage("The seat cannot be locked.");
		
		if (count < 1) {
			throw new ICinemaLockCinemaException("The seat cannot be locked.", cinema_exc);
		}
		
		if (!seat_statuses.containsKey(seat.getRow() + seat.getColumn())) {
			throw new ICinemaLockCinemaException("The seat cannot be locked.", cinema_exc);
		}

		for (int i=0; i < count; i++) {
			int column = Integer.parseInt(seat.getColumn()) + i;
			String key = seat.getRow() + String.valueOf(column);
			if (seat_statuses.containsKey(key)) {
				if (seat_statuses.get(key) == SeatStatus.FREE) {
					seat_statuses.replace(key, SeatStatus.LOCKED);
				}
				else fault = true;
			}
			else fault = true;
			if (fault) {
				for (int j=0; j < i; j++) {
					column = Integer.parseInt(seat.getColumn()) + j;
					key = seat.getRow() + String.valueOf(column);
					seat_statuses.replace(key, SeatStatus.FREE);
				}
				throw new ICinemaLockCinemaException("The seat cannot be locked.", cinema_exc);
			}
		}
		
		String id = String.valueOf(count) + seat.getRow() + seat.getColumn();
		Lock lock = new Lock();
		lock.setSeat(seat);
		lock.setCount(count);
		seat_locks.put(id, lock);
		return id;
	}

	@Override
	public void unlock(String lockId) throws ICinemaUnlockCinemaException {
		CinemaException cinema_exc = new CinemaException();
		cinema_exc.setErrorCode(5);
		cinema_exc.setErrorMessage("Cannot unlock this lock id.");
		
		if (seat_locks.containsKey(lockId)) {
			Seat seat = seat_locks.get(lockId).getSeat();
			int count = seat_locks.get(lockId).getCount();
			
			for (int i = 0; i < count; i++) {
				int column = Integer.parseInt(seat.getColumn()) + i;
				String key = seat.getRow() + String.valueOf(column);
				
				if (seat_statuses.get(key) != SeatStatus.LOCKED) {
					throw new ICinemaUnlockCinemaException("Cannot unlock this lock id.", cinema_exc);
				}
				else {
					seat_statuses.replace(key, SeatStatus.FREE);
				}
			}
		}
		else {
			throw new ICinemaUnlockCinemaException("Cannot unlock this lock id.", cinema_exc);
		}
		
		seat_locks.remove(lockId);
	}

	@Override
	public void reserve(String lockId) throws ICinemaReserveCinemaException {
		CinemaException cinema_exc = new CinemaException();
		cinema_exc.setErrorCode(6);
		cinema_exc.setErrorMessage("Cannot reserve these seats.");
		
		if (seat_locks.containsKey(lockId)) {
			Seat seat = seat_locks.get(lockId).getSeat();
			int count = seat_locks.get(lockId).getCount();
			
			for (int i = 0; i < count; i++) {
				int column = Integer.parseInt(seat.getColumn()) + i;
				String key = seat.getRow() + String.valueOf(column);
				
				if (seat_statuses.get(key) != SeatStatus.LOCKED) {
					throw new ICinemaReserveCinemaException("Cannot reserve these seats.", cinema_exc);
				}
				else {
					seat_statuses.replace(key, SeatStatus.RESERVED);
				}
			}
		}
		else {
			throw new ICinemaReserveCinemaException("Cannot reserve these seats.", cinema_exc);
		}
	}

	@Override
	public void buy(String lockId) throws ICinemaBuyCinemaException {
		CinemaException cinema_exc = new CinemaException();
		cinema_exc.setErrorCode(7);
		cinema_exc.setErrorMessage("Cannot buy these seats.");
		
		if (seat_locks.containsKey(lockId)) {
			Seat seat = seat_locks.get(lockId).getSeat();
			int count = seat_locks.get(lockId).getCount();
			
			for (int i = 0; i < count; i++) {
				int column = Integer.parseInt(seat.getColumn()) + i;
				String key = seat.getRow() + String.valueOf(column);
				
				if (seat_statuses.get(key) == SeatStatus.LOCKED || seat_statuses.get(key) == SeatStatus.RESERVED) {
					seat_statuses.replace(key, SeatStatus.SOLD);
				}
				else {
					throw new ICinemaBuyCinemaException("Cannot buy these seats.", cinema_exc);
				}
			}
		}
		else {
			throw new ICinemaBuyCinemaException("Cannot buy these seats.", cinema_exc);
		}
	}
}
