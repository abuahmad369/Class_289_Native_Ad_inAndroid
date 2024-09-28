package com.example.class_289_native_ad_inandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String,String>> arrayList;
    ArrayList<HashMap<String,String>> finalArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

//        ------------Admob initialize-----------------

        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();
//        ----------------------------------------


        createMainItems();
        createFinalItems();

        XAdapter adapter = new XAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private class XAdapter extends RecyclerView.Adapter{

        int BOOK_ITEM = 0;
        int VIDEO_ITEM = 1;
        int NATIVE_AD_ITEM = 2;
        private class bookViewHolder extends RecyclerView.ViewHolder{

            ImageView imgBook;
            TextView tvBookName,tvWriterName;
            MaterialButton buttonGetBook;

            public bookViewHolder(@NonNull View itemView) {
                super(itemView);

                imgBook = itemView.findViewById(R.id.imgBook);
                tvBookName = itemView.findViewById(R.id.tvBookName);
                tvWriterName = itemView.findViewById(R.id.tvWriterName);
                buttonGetBook = itemView.findViewById(R.id.buttonGetBook);
            }
        }

        private class videoViewHolder extends RecyclerView.ViewHolder{

            TextView tvVideoTitle;
            ImageView imgThumnail;

            public videoViewHolder(@NonNull View itemView) {
                super(itemView);

                tvVideoTitle = itemView.findViewById(R.id.tvVideoTitle);
                imgThumnail = itemView.findViewById(R.id.imgThumnail);
            }
        }

        private class nativeAdViewHolder extends RecyclerView.ViewHolder{

            TemplateView templateView;

            public nativeAdViewHolder(@NonNull View itemView) {
                super(itemView);

                templateView = itemView.findViewById(R.id.templateView);

            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();

            if(viewType == BOOK_ITEM){
                View myView = inflater.inflate(R.layout.item_book, parent, false);
                return new bookViewHolder(myView);

            }
            else if(viewType == NATIVE_AD_ITEM){
                View myView = inflater.inflate(R.layout.item_native_ad, parent, false);
                return new nativeAdViewHolder(myView);

            }else {
                View myView = inflater.inflate(R.layout.item_video,parent,false);
                return new videoViewHolder(myView);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if(getItemViewType(position)==BOOK_ITEM){
                bookViewHolder myHolder = (bookViewHolder) holder;

                hashMap = finalArrayList.get(position);

                String bookName = hashMap.get("bookName");
                String writerName = hashMap.get("writerName");
                String bookLink = hashMap.get("bookLink");
                String bookImage = hashMap.get("bookImage");

                myHolder.tvBookName.setText(bookName);
                myHolder.tvWriterName.setText(writerName);

                myHolder.buttonGetBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), bookLink, Toast.LENGTH_SHORT).show();
                    }
                });
                Picasso.get().load(bookImage).into(myHolder.imgBook);

            } else if (getItemViewType(position)==VIDEO_ITEM) {
                videoViewHolder myHolder = (videoViewHolder) holder;

                hashMap = finalArrayList.get(position);

                String videoTtile = hashMap.get("videoTtile");
                String videoId = hashMap.get("videoId");

                String imgUrl = "https://img.youtube.com/vi/"+videoId+"/0.jpg";

                myHolder.tvVideoTitle.setText(videoTtile);
                Picasso.get().load(imgUrl).into(myHolder.imgThumnail);

            }

            else if (getItemViewType(position)==NATIVE_AD_ITEM) {
                nativeAdViewHolder myHolder = (nativeAdViewHolder) holder;

                MobileAds.initialize(MainActivity.this);
                AdLoader adLoader = new AdLoader.Builder(MainActivity.this, "ca-app-pub-3940256099942544/2247696110")
                        .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd nativeAd) {

                                myHolder.templateView.setNativeAd(nativeAd);
                            }
                        })
                        .build();

                adLoader.loadAd(new AdRequest.Builder().build());

            }

        }

        @Override
        public int getItemCount() {
            return finalArrayList.size();
        }


        @Override
        public int getItemViewType(int position) {

            hashMap = finalArrayList.get(position);
            String itemType = hashMap.get("itemType");

            if(itemType.contains("BOOK")){
                return BOOK_ITEM;
            } else if (itemType.contains("NATIVE_AD")) {
                return NATIVE_AD_ITEM;

            } else return VIDEO_ITEM;

        }
    }

    private void createMainItems(){
        arrayList = new ArrayList<>();

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "BOOK");
        hashMap.put("bookName", "হাউ টু এনালাইজ পিপল লাইক শালর্ক");
        hashMap.put("writerName", "by প্যাট্রিক লাইটম্যান ");
        hashMap.put("bookLink", "https://www.rokomari.com/book/262822/the-four-agreements");
//        https://randomimg.almahmud.top/public
        hashMap.put("bookImage", "https://s3-ap-southeast-1.amazonaws.com/rokomari110/ProductNew20190903/130X186/How_To_Analyze_People_Like_Sherlock-Patrick_Lightman-ee989-423584.png");
        arrayList.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("itemType", "VIDEO");
        hashMap.put("videoTtile", "সূরা কাহফ");
        hashMap.put("videoId", "lW7rrG8oGL8");
        arrayList.add(hashMap);
    }

//    =================================

    private void createFinalItems(){

        finalArrayList = new ArrayList<>();

        for(int x = 0; x<arrayList.size(); x++){

            if(x==1 || x==11 || x==19){
//            if(x>1 && x%5 == 0)
                hashMap = new HashMap<>();
                hashMap.put("itemType", "NATIVE_AD");
                finalArrayList.add(hashMap);
            }

            hashMap = arrayList.get(x);
            finalArrayList.add(hashMap);
        }
    }


}