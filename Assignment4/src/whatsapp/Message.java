package whatsapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {

	private Date _Time;
	private User _Source, _Target;
	private String _Message;

	public Message(User source, User target, String message) {
		_Source = source;
		_Target = target;
		_Time = Calendar.getInstance().getTime();
		_Message = message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(new SimpleDateFormat("dd/MM/yyyy HH:mm ").format(_Time))
				.append("\n").append(_Source.getName()).append(": ")
				.append(_Message).append("\nTo: ").append(_Target.getName())
				.append("\n");
		return builder.toString();
	}
}
