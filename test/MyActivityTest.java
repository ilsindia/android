
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.crinis.solar.LoginActivity;
import com.crinis.solar.R;



@RunWith(RobolectricTestRunner.class)
public class MyActivityTest {

    @Test
    public void shouldHaveHappySmiles() throws Exception {
        String hello = new LoginActivity().getResources().getString(R.string.appname);
        assertThat(hello, equalTo("Crinis"));
    }

}