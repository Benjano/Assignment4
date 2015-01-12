package whatsapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {

	private Date _Time;
	private String _Source, _Target;
	private String _Message;

	public Message(String source, String target, String message) {
		_Source = source;
		_Target = target;
		_Time = Calendar.getInstance().getTime();
		_Message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(new SimpleDateFormat("dd/MM/yyyy HH:mm ").format(_Time))
				.append("\n").append(_Source).append(": ")
				.append(_Message).append("\nTo: ").append(_Target)
				.append("\n");
		return builder.toString();
	}

	public String getSource() {
		return _Source;
	}

	public String getTarget() {
		return _Target;
	}

	
	
}
