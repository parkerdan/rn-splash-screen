package com.parkerdan.rnsplashscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

public class RNSplashScreen {
   private static final int UIAnimationNone = 0;
   private static final int UIAnimationFade = 1;
   private static final int UIAnimationScale = 2;

   private static Dialog dialog;
   private static ImageView imageView;

   private static int dp2px(Context curContext, int dp) {
     return (int) TypedValue.applyDimension(
       TypedValue.COMPLEX_UNIT_DIP,
       dp,
       curContext.getResources().getDisplayMetrics()
     );
   }

   // private static final String TAG = "MyActivity";

  private static int getImage(
    final Activity activity,
    final String imageName
  ) {

      int drawableId = activity.getResources().getIdentifier(
        imageName,
        "drawable",
        activity.getClass().getPackage().getName()
      );

      if (drawableId == 0) {
        drawableId = activity.getResources().getIdentifier(
          imageName,
          "drawable",
          activity.getPackageName()
        );
      }
      return drawableId;
  }

  private static int getLogoDimension(
    final Activity activity,
    final String dimension
  ) {
    int intId = activity.getResources().getIdentifier(
      dimension,
      "integer",
      activity.getClass().getPackage().getName()
    );
    return activity.getResources().getInteger(intId);
  }


  public static void show(final Activity activity) {

      if (activity == null) return;

      final int backgroundId = getImage(activity, "background");
      final int logoId = getImage(activity, "logo");

      final int logoHeight = getLogoDimension(activity, "logo_height");
      final int logoWidth = getLogoDimension(activity, "logo_width");


      if ((dialog != null && dialog.isShowing())||(backgroundId == 0)) {
        return;
      }
      activity.runOnUiThread(new Runnable() {
        public void run() {

          Context context = activity;
          RelativeLayout relativeLayout = new RelativeLayout(context);

          RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
          );

          relativeLayout.setLayoutParams(rlp);

          relativeLayout.setBackground(
            ContextCompat.getDrawable(context, backgroundId)
          );

          imageView = new ImageView(context);

          imageView.setImageResource(logoId);


          RelativeLayout.LayoutParams lp =
            new RelativeLayout.LayoutParams(
              RelativeLayout.LayoutParams.WRAP_CONTENT,
              RelativeLayout.LayoutParams.WRAP_CONTENT
          );

          lp.addRule(RelativeLayout.CENTER_IN_PARENT);

          imageView.setLayoutParams(lp);

          imageView.getLayoutParams().height = dp2px(context, logoHeight);
          imageView.getLayoutParams().width = dp2px(context, logoWidth);

          relativeLayout.addView(imageView);


          dialog = new Dialog(
            context,
            android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
          );

          dialog.setContentView(relativeLayout);
          dialog.setCancelable(false);
          dialog.show();
        }
      }
    );
  }

  public static void remove(
    Activity activity,
    final String animationType,
    final int duration
  ) {
    if (activity == null) {
        if(activity == null) return;
    }
    activity.runOnUiThread(new Runnable() {
      public void run() {
        if (dialog != null && dialog.isShowing()) {
          AnimationSet animationSet = new AnimationSet(true);

          if(animationType.equals("scale")) {
            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(duration);
            animationSet.addAnimation(fadeOut);

            ScaleAnimation scale = new ScaleAnimation(
              1,
              1.5f,
              1,
              1.5f,
              Animation.RELATIVE_TO_SELF,
              0.5f,
              Animation.RELATIVE_TO_SELF,
              0.65f
            );
            scale.setDuration(duration);
            animationSet.addAnimation(scale);
          } else if (animationType.equals("fade")) {

            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(duration);
            animationSet.addAnimation(fadeOut);
          } else {
            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(0);
            animationSet.addAnimation(fadeOut);
          }

          final View view = (
            (ViewGroup)dialog.getWindow().getDecorView()
          ).getChildAt(0);
          view.startAnimation(animationSet);

          animationSet.setAnimationListener(
            new Animation.AnimationListener()
          {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
              view.post(new Runnable() {
                @Override
                public void run() {
                  dialog.dismiss();
                  dialog = null;
                  imageView = null;
                }
              });
            }
          });
        }
      }
    });
  }

}
