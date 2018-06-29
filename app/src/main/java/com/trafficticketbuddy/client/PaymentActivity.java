package com.trafficticketbuddy.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.trafficticketbuddy.client.apis.ApiAcceptBids;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class PaymentActivity extends BaseActivity {
    public Card card;
    private EditText et_month,et_year,et_cvv;
    private String[] month;
    private int[] f;
    private EditText etCardnumber;
    private CardView cardPay,cardPaymantFields;
    private int[] year;
    private LinearLayout linSuccessPage;
    private String amount="0";
    private String bidID;
    private String caseID;
    private Toolbar toolbar;
    private TextView tvAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        linSuccessPage=(LinearLayout)findViewById(R.id.linSuccessPage);
        cardPaymantFields=(CardView)findViewById(R.id.cardPaymantFields);
        cardPay=(CardView)findViewById(R.id.cardPay);
        tvAmount=(TextView)findViewById(R.id.tvAmount);
        etCardnumber=(EditText)findViewById(R.id.etCardnumber);
        et_cvv=(EditText)findViewById(R.id.et_cvv);
        et_month=(EditText)findViewById(R.id.et_month);
        et_year=(EditText)findViewById(R.id.et_year);
        cardPaymantFields.setVisibility(View.VISIBLE);
        linSuccessPage.setVisibility(View.GONE);


        if (getIntent().getStringExtra("amount")!=null){
            amount=getIntent().getStringExtra("amount");
            bidID=getIntent().getStringExtra("bid_id");
            caseID=getIntent().getStringExtra("case_id");
            tvAmount.setText(amount);
        }

        et_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMonth(v);

            }
        });
        et_year.setOnClickListener(new View.OnClickListener() {
            public int year;

            @Override
            public void onClick(View v) {

                getYear(v);
            }
        });
        cardPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setCardDetails(etCardnumber.getText().toString(),Integer.parseInt(month[0]),Integer.parseInt(et_year.getText().toString()),et_cvv.getText().toString());
            }
        });
    }

    public void setCardDetails(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        card = new Card(
                cardNumber,
                cardExpMonth,
                cardExpYear,
                cardCVC
        );

        card.validateNumber();
        card.validateCVC();
        if (card.validateCard()){
            callCard();
        }
    }

    public void callCard() {
        Stripe stripe = new Stripe(PaymentActivity.this, "pk_test_XZfUvDhE0FNbQMwCiknJaQqv");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        System.out.println("!!!!!!!!!!"+token);
                        // Send token to your server
                        callPostToken(token.getId());
                    }

                    public void onError(Exception error) {
                        // Show localized error message

                        ;
                    }
                }
        );
    }

    private void callPostToken(String token) {
        if (isValidate()){
            if (isNetworkConnected())
            apiClient(token,amount);
        }

    }

    private boolean isValidate() {
        if (etCardnumber.getText().toString().isEmpty()){
            showDialog("Please enter the card number");
            return false;

        }else if (et_month.getText().toString().isEmpty()){
            showDialog("Please select expiry month on the card");
            return false;

        }else if (et_year.getText().toString().isEmpty()){
            showDialog("Please select expiry year on the card");
            return false;

        }else if (et_cvv.getText().toString().isEmpty()){
            showDialog("Please enter CVV number on the card");
            return false;

        }
        return true;
    }


    public  void  apiClient(String token,String price) {

      Retrofit retrofit = new Retrofit.Builder()
              .baseUrl("http://13.58.150.208/buddy/")
              .addConverterFactory(GsonConverterFactory.create())
              .build();

        RetrofitService service = retrofit.create(RetrofitService.class);



        Call<ResponseBody> call = service.paymentApi(token,price);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res=response.body().string();
                    JSONObject object=new JSONObject(res);
                    if (object.getBoolean("status")){
                        callAcceptBid(bidID,caseID);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void callSuccess() {
        cardPaymantFields.setVisibility(View.GONE);
        linSuccessPage.setVisibility(View.VISIBLE);
    }


    public interface RetrofitService {
        @FormUrlEncoded
        @POST("stripe.php")
        Call<ResponseBody> paymentApi(@Field("stripeToken") String stripeToken,@Field("price") String price );
    }


















    public void getMonth(View button1) {
        PopupMenu popup = new PopupMenu(PaymentActivity.this, button1);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_month, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();

                String[] splited = title.split("\\s+");
                month=splited;
                et_month.setText(month[0]);
             //   Toast.makeText(MainActivity.this, "You Clicked : " +month[0], Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        popup.show();//showing popup menu

    }
    public void getYear(View button1) {

        PopupMenu popup = new PopupMenu(PaymentActivity.this, button1);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_year, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                String substring = title.substring(Math.max(title.length() - 2, 0));
                et_year.setText(substring);
              //  Toast.makeText(MainActivity.this, "You Clicked : " + year[0], Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    private void callAcceptBid(String id, String caseId) {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiAcceptBids(getParamAccept(id,caseId), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                           // showDialog(object.getString("message"));
                            //getBids(getIntent().getStringExtra("case_id"));
                           // finish();
                            callSuccess();
                        }
                        else
                            showDialog(object.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }
    }

    private Map<String, String> getParamAccept(String id, String caseId) {

        Map<String,String> map=new HashMap<>();
        map.put("case_id",caseId);
        map.put("bid_id",id);
        return map;
    }
}

