
package org.airbloc.airblocsample2.app.settings;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;

import org.airbloc.airblocsample2.Constants;
import org.airbloc.airblocsample2.R;
import org.airbloc.airblocsample2.app.SolinActivity;
import org.airbloc.airblocsample2.views.SolinTextView;

public class HelpActivity extends SolinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        setUpToolbar();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        WebView webview = (WebView) findViewById(R.id.termsWebView);
        webview.loadUrl(Constants.SOLIN_WEB_URL); // TODO: temporary page until help finish

        SolinTextView title = findView(R.id.title);
        title.setText("도움말");
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
