[![Build Status](https://travis-ci.org/amatkivskiy/complex-up-navigation.svg?branch=master)](https://travis-ci.org/amatkivskiy/complex-up-navigation)

# Flexible Up Navigation
This project demonstrates one of the ways how to implement up navigation in Android app when one activity can have multiple parent activities.

It is implemented throuhh passing the parent activity link (for example class of the activity) to the child activity.
```
public static Intent createIntent(Context context, int contentId, Class<?> referrer) {
  Intent intent = new Intent(context, ContentDetailsActivity.class);
  intent.putExtra(CONTENT_ID, contentId);

  Referrers.putReferrer(intent, referrer);

  return intent;
}
```

Then when you handle up button click you can check whether you have this referrer. And if yes then just open referrer activity, but if not then recreate back stack manually
```
// Check whether back stack exists.
if (Referrers.hasReferrer(getIntent())) {
  // If the current back stack exists just navigate to parent activity.
  Class parentActivityClass = Referrers.getReferrer(getIntent());
  Intent intent = new Intent(this, parentActivityClass).setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);

  this.startActivity(intent);
} else {
  // If ItemDetailsActivity was open for example from the notification then create back stack manually.
  Intent collectionsIntent = ContentCollectionActivity.createIntent(this, null);
  Intent mainActivityIntent = new Intent(this, MainActivity.class);

  TaskStackBuilder.create(this)
    .addNextIntent(mainActivityIntent)
    .addNextIntent(collectionsIntent)
    .startActivities();
}
```

## For more details on what is the problem with Up Navigation please read [Clear way for exciting Up Navigation](https://medium.com/@amatkivskiy/clear-way-for-exciting-up-navigation-a66153296ae0)

# This is sample project for https://medium.com/@amatkivskiy/clear-way-for-exciting-up-navigation-a66153296ae0


