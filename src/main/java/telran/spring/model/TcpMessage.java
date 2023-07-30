package telran.spring.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class TcpMessage extends Message {

	public String hostName;
	
	@Min(value = 1024) @Max(5000)
	public int port;
	
}
