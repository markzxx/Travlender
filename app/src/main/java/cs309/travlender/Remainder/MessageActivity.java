package cs309.travlender.Remainder;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cs309.travelender.R;

public class MessageActivity extends AppCompatActivity  {

	private MediaPlayer mediaPlayer;

	@Bind(R.id.texttitle)
	TextView texttitle;

	@Bind(R.id.textcontent)
	TextView textcontent;

	@Bind(R.id.button)
	Button button;
	@OnClick(R.id.button)
	void close(){
		stop();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remainder_message);
		ButterKnife.bind(this);
		mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
		Intent intent=getIntent();
		String title = intent.getStringExtra("title");
		String content = intent.getStringExtra("content");
		texttitle.setText(title);
		textcontent.setText(content);
	}

	public void stop(){
		mediaPlayer.stop();
		finish();
	}
}
