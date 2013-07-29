import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * [2013-03-26] hskang Log를 D:/temp/koneki_simulator.log 파일에 남긴다.
 */

public class Log {

	public static void write(String msg) {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String timeStr = "[" + sdf.format(cal.getTime()) + "]";
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"D:/temp/koneki_simulator.log", true));
			out.write(timeStr + " " + msg);
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printStackTrace() {
		String lineSep = System.getProperty("line.separator");
		StringBuffer stacktrace = new StringBuffer();
		StackTraceElement[] stackTrace = new Exception().getStackTrace();

		for (int x = 0; x < stackTrace.length; x++) {
			stacktrace.append(stackTrace[x].toString()).append(lineSep);
		}

		write(stacktrace.toString());
	}
}