# Mode-View-Presenter
##Using the Butter Knife Library

    PUPPOSE FOR USING BUTTER KNIFE LIBRARY
##----------------------------------------------$$
		In every Android application, you have to use the findViewById() method for each view in the layout that you want to use in your application's code. But as applications' designs get more complex layouts, the call to this method becomes repetitive and this is where the Butter Knife library comes in.

	HOW CAN I APPLY THE BUTTER KNIFE LIBRARY
##----------------------------------------------$$
	Step 1: Add the dependency

		compile 'com.jakewharton:butterknife:6.1.0'
	Step 2: Use the Annotations

		@InjectView(R.id.sample_textview)
		TextView sample_textview;

	Step 3: Inject Views

		In the onCreate() method of the activity, before using any 			the views, call inject on the Butterknife object.

		[Activity] ButterKnife.inject(this)

		If you are using fragments, you have to specify the source 			of the views in the onCreateView() method as shown below.
		
		[Fragment] View view = inflater.inflate(R.layout.sample_fragment, null);
		ButterKnife.inject(this, view);
		
	=> READ MORE: https://code.tutsplus.com/tutorials/quick-tip-using-butter-knife-to-inject-views-on-android--cms-23542

##Create Application Android That demonstrate MVP Android

