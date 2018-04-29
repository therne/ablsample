
package org.airbloc.airblocsample2.app.settings;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;

import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinActivity;
import org.airbloc.airblocsample2.views.SolinTextView;

public class TermsActivity extends SolinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setUpToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        WebView webview = (WebView) findViewById(R.id.termsWebView);
        webview.loadUrl(Constants.TERMS_URL);

        SolinTextView title = findView(R.id.title);
        title.setText("개인정보 취급방침 및 이용 약관");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
