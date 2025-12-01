package stream.okchun.dashboard.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

@Slf4j
@Component
public class RequestIdGenerator extends OncePerRequestFilter {
	static final String SERVER_KEY;

	static {
		String SERVER_KEY_TMP;
		try {
			SERVER_KEY_TMP = InetAddress.getLocalHost().getHostName() + getServerKey();
		} catch (UnknownHostException e) {
			SERVER_KEY_TMP = getServerKey();
		}
		SERVER_KEY = SERVER_KEY_TMP;
	}

	private static String getServerKey() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		return salt.toString();

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		request.setAttribute("REQUEST_TRACE_ID", SERVER_KEY + "_" + request.getRequestId());
		filterChain.doFilter(request, response);
	}
}
