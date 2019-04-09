package com.montek.tohafaa;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.*;
import android.view.MenuItem;
import android.widget.TextView;
public class TermsCondition extends AppCompatActivity{
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termcondition);
         ActionBar actionBar = getSupportActionBar();
         assert actionBar != null;
         actionBar.setHomeButtonEnabled(true);
         TextView txv_content = (TextView) findViewById(R.id.txv_content);
         txv_content.setText(Html.fromHtml(getString(R.string.html)));
         txv_content.setTextAppearance(getBaseContext(), R.style.SimpleStyle);
     }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
}
