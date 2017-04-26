package com.majorproject.groceryshopping.sellingapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFaqMainSeller extends Fragment {

    private TextView textViewFaq;

    public FragmentFaqMainSeller() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faq_main_seller, container, false);
        String html = getString(R.string.html_faq);
        Spanned htmlText = null;
        if (android.os.Build.VERSION.SDK_INT < 24 )
        {
            htmlText = Html.fromHtml(html);
        }
        else
        {
            htmlText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        }



        textViewFaq = (TextView)view.findViewById(R.id.faq_text_view);
        textViewFaq.setText(htmlText);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq_main_seller, container, false);

    }

}
