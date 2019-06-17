package banking;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import banking.Banking.ChargeCardRequest;
import banking.Banking.ChargeCardResponse;
import com.google.protobuf.util.*;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;

public class Hello implements IHello_b {

	@Override
	public Response ChargeCard(InputStream input) {
		try {
			ChargeCardRequest request = ChargeCardRequest.parseFrom(input);
			String cardNumber = request.getCardNumber();
			int amount = request.getAmount();
			
			boolean success = false;
			if (amount>0 && cardNumber.length()%2 == 0) success = true;

			ChargeCardResponse response = ChargeCardResponse.newBuilder().setSuccess(success).build();
			return Response.ok(response.toByteArray()).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.serverError().build();
	}

	@Override
	public Response ChargeCard_json(String input) {
		try {
			ChargeCardRequest.Builder request_builder = ChargeCardRequest.newBuilder();
			JsonFormat.parser().merge(input, request_builder);
			ChargeCardRequest request = request_builder.build();
			String cardNumber = request.getCardNumber();
			int amount = request.getAmount();
			
			boolean success = false;
			if (amount>0 && cardNumber.length()%2 == 0) success = true;
			
			
			ChargeCardResponse response = ChargeCardResponse.newBuilder().setSuccess(success).build();
			String json = JsonFormat.printer().print(response);
			return Response.ok(json).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.serverError().build();
	}

}
