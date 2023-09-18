package com.example.translate_dectionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText sourceLanguageEt;
    private Button lookup;
    private TextView destinationLangugeTv;
    private MaterialButton sourceLangugeChooseBtn , destinationlanguageChooseBtn , translateBtn;

    private TranslatorOptions translatorOptions;

    private Translator translator;

    private ProgressDialog progressDialog;

    private ArrayList<ModelLanguage> languageArrayList;

    private  static  final String TAG = "MAIN_TAG";

    private  String sourceLanguageCode = "en";
    private  String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "vi";
    private String destinationLanguageTitle = "vietnamses";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceLanguageEt= findViewById(R.id.sourceLanguageEt);
        destinationLangugeTv = findViewById(R.id.destinationLangugeTv);
        sourceLangugeChooseBtn = findViewById(R.id.sourceLangugeChooseBtn);
        destinationlanguageChooseBtn = findViewById(R.id.destinationlanguageChooseBtn);
        translateBtn = findViewById(R.id.translateBtn);
        lookup = (Button) findViewById(R.id.btn_lookup);

        lookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, anhviet.class);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("pleace wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadAvailableLanguage();

        sourceLangugeChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sourceLanguageChoose();
            }
        });
        destinationlanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               destinationChooseBtn();
            }
        });
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }
    private String sourceLanguageText =" ";

    private void validateData() {
        sourceLanguageText = sourceLanguageEt.getText().toString().trim();
        Log.d(TAG,"validateData : sourceLanguageText:" + sourceLanguageText );

        if(sourceLanguageText.isEmpty()){
            Toast.makeText(this, "Enter text to Translate...", Toast.LENGTH_SHORT).show();
        }else {
            startTranslations();
        }
    }

    private void startTranslations() {

        progressDialog.setMessage("Processing language model...");
        progressDialog.show();

        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();
        translator = Translation.getClient(translatorOptions);

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: model ready , staring translate...");

                        progressDialog.setMessage("Translating...");

                        translator.translate(sourceLanguageText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {

                                        Log.d(TAG, "onSuccess: translatedText:"+ translatedText);
                                        progressDialog.dismiss();
                                        destinationLangugeTv.setText(translatedText);


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Log.d(TAG, "onFailure: ",e );
                                        Toast.makeText(MainActivity.this, "Failed to translate due to ..."+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: " , e );
                        Toast.makeText(MainActivity.this, "Failed to ready model due to..."+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sourceLanguageChoose(){
        PopupMenu popupMenu = new PopupMenu(this,sourceLangugeChooseBtn);

        for(int i =0 ; i< languageArrayList.size();i++){

            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);

        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();

                sourceLanguageCode = languageArrayList.get(position).laguageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;

                sourceLangugeChooseBtn.setText(sourceLanguageTitle);
                sourceLanguageEt.setHint("Enter" + sourceLanguageTitle);

                Log.d(TAG, "onMenuItemClick : sourceLanguageCode" + sourceLanguageCode);
                Log.d(TAG, "onMenuItemClick : sourceLanguageTitle" + sourceLanguageTitle);


                return false;
            }
        });
    }

    private void destinationChooseBtn(){

        PopupMenu popupMenu = new PopupMenu(this,destinationlanguageChooseBtn);

        for (int i =0 ; i< languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();

                destinationLanguageCode = languageArrayList.get(position).laguageCode;
                destinationLanguageTitle = languageArrayList.get(position).languageTitle;

                destinationlanguageChooseBtn.setText(destinationLanguageTitle);

                Log.d(TAG, "onMenuItemClick:destinationLanguageCode"+destinationLanguageCode);
                Log.d(TAG, "onMenuItemClick:destinationLanguageTitle"+destinationLanguageTitle);


                return false;
            }
        });
    }

    private void loadAvailableLanguage() {
        languageArrayList = new ArrayList<>();


        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for (String languageCode : languageCodeList ){

            String languageTitle = new Locale(languageCode).getDisplayLanguage();

            Log.d(TAG,"loadAvailableLanguages: languageCode" + languageCode);
            Log.d(TAG,"loadAvailableLanguages: languageTitle" + languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode , languageTitle);
            languageArrayList.add(modelLanguage);



        }

    }
}