package datemessage;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class DateMessageProvideTest {

	@Test
	public void 오전() throws Exception {
		DateMessageProvider provider = new DateMessageProvider();
		System.out.println(provider.getDateMessage());
		assertThat(provider.getDateMessage(), is("오전"));
	}

	@Test
	public void 오후() throws Exception {
		DateMessageProvider provider = new DateMessageProvider();
		System.out.println(provider.getDateMessage());
		assertThat(provider.getDateMessage(), is("오후"));
	}

}
