package org.openintents.shopping.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.DocumentsContract;
import androidx.core.content.ContextCompat;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.widget.Toast;
import android.provider.Settings;

import org.openintents.shopping.R;
import org.openintents.shopping.library.provider.ShoppingContract.Contains;
import org.openintents.shopping.library.provider.ShoppingContract.Lists;
import org.openintents.shopping.library.util.ShoppingUtils;
import org.openintents.shopping.provider.ShoppingDatabase;
import org.openintents.shopping.ui.widget.ShoppingItemsView;
import org.openintents.util.BackupManagerWrapper;
import org.openintents.util.IntentUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PreferenceActivity extends android.preference.PreferenceActivity
        implements OnSharedPreferenceChangeListener {
    public static final String PREFS_SAMESORTFORPICK = "samesortforpick";
    public static final boolean PREFS_SAMESORTFORPICK_DEFAULT = false;
    public static final String PREFS_SORTORDER = "sortorder";
    public static final String PREFS_PICKITEMS_SORTORDER = "sortorderForPickItems";
    public static final String PREFS_SORTORDER_DEFAULT = "3";
    public static final String PREFS_PICKITEMS_SORTORDER_DEFAULT = "1";
    public static final String PREFS_SORTORDER_SHOPPINGLISTS = "sortorderForShoppingLists";
    public static final String PREFS_SORTORDER_SHOPPINGLISTS_DEFAULT = "0";
    public static final String PREFS_FONTSIZE = "fontsize";
    public static final String PREFS_FONTSIZE_DEFAULT = "2";
    public static final String PREFS_ORIENTATION = "orientation";
    public static final String PREFS_ORIENTATION_DEFAULT = "-1";
    @Deprecated
    public static final String PREFS_LOADLASTUSED = "loadlastused";
    @Deprecated
    public static final boolean PREFS_LOADLASTUSED_DEFAULT = true;
    public static final String PREFS_LASTUSED = "lastused";
    public static final String PREFS_LASTLIST_POSITION = "lastlist_position";
    public static final String PREFS_LASTLIST_TOP = "lastlist_top";
    public static final String PREFS_HIDECHECKED = "hidechecked";
    public static final boolean PREFS_HIDECHECKED_DEFAULT = false;
    public static final String PREFS_FASTSCROLL = "fastscroll";
    public static final boolean PREFS_FASTSCROLL_DEFAULT = false;
    public static final String PREFS_CAPITALIZATION = "capitalization";
    public static final String PREFS_SHOW_PRICE = "showprice";
    public static final boolean PREFS_SHOW_PRICE_DEFAULT = true;
    public static final String PREFS_PERSTOREPRICES = "perstoreprices";
    public static final boolean PREFS_PERSTOREPRICES_DEFAULT = false;
    public static final String PREFS_SHOW_TAGS = "showtags";
    public static final boolean PREFS_SHOW_TAGS_DEFAULT = true;
    public static final String PREFS_SHOW_QUANTITY = "showquantity";
    public static final boolean PREFS_SHOW_QUANTITY_DEFAULT = true;
    public static final String PREFS_SHOW_UNITS = "showunits";
    public static final boolean PREFS_SHOW_UNITS_DEFAULT = true;
    public static final String PREFS_SHOW_PRIORITY = "showpriority";
    public static final boolean PREFS_SHOW_PRIORITY_DEFAULT = true;
    public static final String PREFS_SCREENLOCK = "screenlock";
    public static final boolean PREFS_SCREENLOCK_DEFAULT = false;
    public static final String PREFS_SHOWINLOCKSCREEN = "showinlockscreen";
    public static final boolean PREFS_SHOWINLOCKSCREEN_DEFAULT = false;
    public static final String PREFS_EXTERNALDB = "externaldb";
    public static final boolean PREFS_EXTERNALDB_DEFAULT = false;
    public static final String PREFS_EXTERNALDB_USED = "externaldb_used";
    public static final boolean PREFS_EXTERNALDB_USED_DEFAULT = false;
    public static final String PREFS_EXTERNALDB_PATH = "externaldb_path";
    public static final String PREFS_EXTERNALDB_PATH_DEFAULT = "";
    public static final String PREFS_RESETQUANTITY = "resetquantity";
    public static final boolean PREFS_RESETQUANTITY_DEFAULT = false;
    public static final String PREFS_ADDFORBARCODE = "addforbarcode";
    public static final boolean PREFS_ADDFORBARCODE_DEFAULT = false;
    public static final String PREFS_SHAKE = "shake";
    public static final boolean PREFS_SHAKE_DEFAULT = false;
    public static final String PREFS_MARKET_EXTENSIONS = "preference_market_extensions";
    public static final String PREFS_MARKET_THEMES = "preference_market_themes";
    public static final String PREFS_THEME_SET_FOR_ALL = "theme_set_for_all";
    public static final String PREFS_SCREEN_ADDONS = "preference_screen_addons";
    public static final String PREFS_PRIOSUBTOTAL = "priority_subtotal_threshold";
    public static final String PREFS_PRIOSUBTOTAL_DEFAULT = "0";
    public static final String PREFS_PRIOSUBINCLCHECKED = "priosubtotal_includes_checked";
    public static final boolean PREFS_PRIOSUBINCLCHECKED_DEFAULT = true;
    public static final String PREFS_PICKITEMSINLIST = "pickitemsinlist";
    public static final boolean PREFS_PICKITEMSINLIST_DEFAULT = false;
    public static final String PREFS_QUICKEDITMODE = "quickedit";
    public static final boolean PREFS_QUICKEDITMODE_DEFAULT = false;
    public static final String PREFS_USE_FILTERS = "use_filters";
    public static final boolean PREFS_USE_FILTERS_DEFAULT = false;
    public static final String PREFS_CURRENT_LIST_COMPLETE = "autocomplete_only_this_list";
    public static final boolean PREFS_CURRENT_LIST_COMPLETE_DEFAULT = true;
    public static final String PREFS_SORT_PER_LIST = "perListSort";
    public static final boolean PREFS_SORT_PER_LIST_DEFAULT = false;
    public static final String PREFS_HOLO_SEARCH = "holosearch";
    public static final boolean PREFS_HOLO_SEARCH_DEFAULT = true;
    public static final String PREFS_SHOW_LAYOUT_CHOICE = "show_layout_choice";
    public static final String PREFS_RESET_ALL_SETTINGS = "reset_all_settings";
    public static final int PREFS_CAPITALIZATION_DEFAULT = 1;
    public static final String EXTRA_SHOW_GET_ADD_ONS = "show_get_add_ons";
    private static final String TAG = "PreferenceActivity";
    private static final TextKeyListener.Capitalize[] smCapitalizationSettings = {
            TextKeyListener.Capitalize.NONE,
            TextKeyListener.Capitalize.SENTENCES,
            TextKeyListener.Capitalize.WORDS};
    private static final int[] smCapitalizationInputTypes = {
            InputType.TYPE_CLASS_TEXT,
            InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
            InputType.TYPE_TEXT_FLAG_CAP_WORDS};
    public static int updateCount;
    private static boolean mBackupManagerAvailable;
    private static boolean mFilterCompletionChanged;

    static final private int ACTIVITY_CHOOSE_DB_PATH = 0;
    static final private int ACTIVITY_FILEMGR_PERMISSION = 1;

    static {
        try {
            BackupManagerWrapper.checkAvailable();
            mBackupManagerAvailable = true;
        } catch (Throwable e) {
            mBackupManagerAvailable = false;
        }
    }

    private ListPreference mPrioSubtotal;
    private CheckBoxPreference mIncludesChecked;
    private CheckBoxPreference mExternaldb;
    private ListPreference mPickItemsSort;

    public static int getFontSizeFromPrefs(Context context) {
        return Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context).getString(PREFS_FONTSIZE,
                        PREFS_FONTSIZE_DEFAULT));
    }

    public static int getOrientationFromPrefs(Context context) {
        return Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context).getString(
                        PREFS_ORIENTATION, PREFS_ORIENTATION_DEFAULT));
    }

    public static boolean getCompleteFromCurrentListOnlyFromPrefs(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREFS_CURRENT_LIST_COMPLETE,
                        PREFS_CURRENT_LIST_COMPLETE_DEFAULT);
    }

    public static boolean getCompletionSettingChanged(Context context) {
        return mFilterCompletionChanged;
    }

    public static boolean getUsingPerStorePricesFromPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_PERSTOREPRICES, PREFS_PERSTOREPRICES_DEFAULT);
    }

    public static boolean getQuickEditModeFromPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_QUICKEDITMODE, PREFS_QUICKEDITMODE_DEFAULT);
    }

    public static boolean getUsingFiltersFromPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_USE_FILTERS, PREFS_USE_FILTERS_DEFAULT);
    }

    public static boolean getUsingHoloSearchFromPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_HOLO_SEARCH, PREFS_HOLO_SEARCH_DEFAULT);
    }

    public static boolean getPickItemsInListFromPrefs(Context context) {
        // boolean using = PreferenceManager.getDefaultSharedPreferences(context)
        //		.getBoolean(PREFS_PICKITEMSINLIST,
        //				PREFS_PICKITEMSINLIST_DEFAULT);
        // return using;
        return true;
    }

    public static boolean getUsingPerListSortFromPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_SORT_PER_LIST,
                        PREFS_SORT_PER_LIST_DEFAULT);
    }

    /**
     * Returns the sort order for the notes list based on the user preferences.
     * Performs error-checking.
     *
     * @param context The context to grab the preferences from.
     */
    static public int getSortOrderIndexFromPrefs(Context context, int mode, long listId) {
        int sortOrder = 0;

        if (mode != ShoppingItemsView.MODE_IN_SHOP) {
            boolean followShopping = PreferenceManager
                    .getDefaultSharedPreferences(context).getBoolean(
                            PREFS_SAMESORTFORPICK,
                            PREFS_SAMESORTFORPICK_DEFAULT);
            if (followShopping) {
                mode = ShoppingItemsView.MODE_IN_SHOP;
            }
        }

        if (mode != ShoppingItemsView.MODE_IN_SHOP) {
            // use the pick-items-specific value, if there is one
            try {
                sortOrder = Integer.parseInt(PreferenceManager
                        .getDefaultSharedPreferences(context).getString(
                                PREFS_PICKITEMS_SORTORDER,
                                PREFS_PICKITEMS_SORTORDER_DEFAULT));
            } catch (NumberFormatException e) {
                // Guess somebody messed with the preferences and put a string
                // into
                // this field. We'll follow shopping mode then.
                mode = ShoppingItemsView.MODE_IN_SHOP;
            }
        }

        if (mode == ShoppingItemsView.MODE_IN_SHOP) {

            boolean set = false;
            if (PreferenceActivity.getUsingPerListSortFromPrefs(context)) {
                String sortOrderStr = ShoppingUtils.getListSortOrder(context,
                        listId);
                if (sortOrderStr != null) {
                    try {
                        sortOrder = Integer.parseInt(sortOrderStr);
                        set = true;
                    } catch (NumberFormatException e) {
                        // Guess somebody messed with the preferences and put a string
                        // into
                        // this field. We'll use the default value then.
                    }
                }
            }

            if (set == false) {
                try {
                    sortOrder = Integer.parseInt(PreferenceManager
                            .getDefaultSharedPreferences(context).getString(
                                    PREFS_SORTORDER, PREFS_SORTORDER_DEFAULT));
                } catch (NumberFormatException e) {
                    // Guess somebody messed with the preferences and put a string
                    // into
                    // this field. We'll use the default value then.
                }
            }
        }

        if (sortOrder >= 0 && sortOrder < Contains.SORT_ORDERS.length) {
            return sortOrder;
        }

        // Value out of range - somebody messed with the preferences.
        return 0;
    }

    static public int getSortOrderIndexFromPrefs(Context context, int mode) {
        long listId = ShoppingUtils.getDefaultList(context);
        return getSortOrderIndexFromPrefs(context, mode, listId);
    }

    static public String getSortOrderFromPrefs(Context context, int mode) {
        int index = getSortOrderIndexFromPrefs(context, mode);
        return Contains.SORT_ORDERS[index];
    }

    static public String getSortOrderFromPrefs(Context context, int mode, long listId) {
        int index = getSortOrderIndexFromPrefs(context, mode, listId);
        return Contains.SORT_ORDERS[index];
    }

    static public boolean prefsStatusAffectsSort(Context context, int mode) {
        int index = getSortOrderIndexFromPrefs(context, mode);
        boolean affects = Contains.StatusAffectsSortOrder[index];
        if (mode == ShoppingItemsView.MODE_IN_SHOP && !affects) {
            // in shopping mode we should also invalidate display when
            // marking items if we are hiding checked items.
            affects = getHideCheckedItemsFromPrefs(context);
        }
        return affects;
    }

    public static String getShoppingListSortOrderFromPrefs(Context context) {
        int index = Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context).getString(
                        PREFS_SORTORDER_SHOPPINGLISTS,
                        PREFS_SORTORDER_SHOPPINGLISTS_DEFAULT));
        if (index >= 0 && index < Lists.SORT_ORDERS.length) {
            return Lists.SORT_ORDERS[index];
        }

        return Lists.DEFAULT_SORT_ORDER;
    }

    public static boolean getHideCheckedItemsFromPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFS_HIDECHECKED, PREFS_HIDECHECKED_DEFAULT);
    }

    public static boolean getFastScrollEnabledFromPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFS_FASTSCROLL, PREFS_FASTSCROLL_DEFAULT);
    }

    private static int getSubtotalByPriorityThreshold(SharedPreferences prefs) {
        String pref = prefs.getString(PREFS_PRIOSUBTOTAL,
                PREFS_PRIOSUBTOTAL_DEFAULT);
        int threshold = 0;
        try {
            threshold = Integer.parseInt(pref);
        } catch (NumberFormatException e) {
            // Guess somebody messed with the preferences and put a string into
            // this
            // field. We'll use the default value then.
        }
        return threshold;
    }

    public static int getSubtotalByPriorityThreshold(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return getSubtotalByPriorityThreshold(prefs);
    }

    public static boolean prioritySubtotalIncludesChecked(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFS_PRIOSUBINCLCHECKED,
                PREFS_PRIOSUBINCLCHECKED_DEFAULT);
    }

    /**
     * Returns a KeyListener for edit texts that will match the capitalization
     * preferences of the user.
     *
     * @ param context The context to grab the preferences from.
     */
    static public KeyListener getCapitalizationKeyListenerFromPrefs(
            Context context) {
        int capitalization = PREFS_CAPITALIZATION_DEFAULT;
        try {
            capitalization = Integer.parseInt(PreferenceManager
                    .getDefaultSharedPreferences(context).getString(
                            PREFS_CAPITALIZATION,
                            Integer.toString(PREFS_CAPITALIZATION_DEFAULT)));
        } catch (NumberFormatException e) {
            // Guess somebody messed with the preferences and put a string
            // into this
            // field. We'll use the default value then.
        }

        if (capitalization < 0
                || capitalization > smCapitalizationSettings.length) {
            // Value out of range - somebody messed with the preferences.
            capitalization = PREFS_CAPITALIZATION_DEFAULT;
        }

        return new TextKeyListener(smCapitalizationSettings[capitalization],
                true);
    }

    /**
     * Returns InputType for the search bar based on the capitalization
     * preferences of the user.
     *
     * @ param context The context to grab the preferences from.
     */
    static public int getSearchInputTypeFromPrefs(
            Context context) {
        int capitalization = PREFS_CAPITALIZATION_DEFAULT;
        try {
            capitalization = Integer.parseInt(PreferenceManager
                    .getDefaultSharedPreferences(context).getString(
                            PREFS_CAPITALIZATION,
                            Integer.toString(PREFS_CAPITALIZATION_DEFAULT)));
        } catch (NumberFormatException e) {
            // Guess somebody messed with the preferences and put a string
            // into this
            // field. We'll use the default value then.
        }

        if (capitalization < 0
                || capitalization > smCapitalizationSettings.length) {
            // Value out of range - somebody messed with the preferences.
            capitalization = PREFS_CAPITALIZATION_DEFAULT;
        }

        return smCapitalizationInputTypes[capitalization];
    }

    public static boolean getThemeSetForAll(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFS_THEME_SET_FOR_ALL, false);
    }

    public static boolean getResetQuantity(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_RESETQUANTITY, PREFS_RESETQUANTITY_DEFAULT);
    }

    public static boolean getAddForBarcode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_ADDFORBARCODE, PREFS_ADDFORBARCODE_DEFAULT);
    }

    public static void setThemeSetForAll(Context context, boolean setForAll) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor ed = prefs.edit();
        ed.putBoolean(PREFS_THEME_SET_FOR_ALL, setForAll);
        ed.commit();
    }

    public static void setUsingHoloSearch(Context context, boolean useHoloSearch) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor ed = prefs.edit();
        ed.putBoolean(PREFS_HOLO_SEARCH, useHoloSearch);
        ed.apply();
    }

    public static boolean getShowLayoutChoice(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getBoolean(PREFS_SHOW_LAYOUT_CHOICE, true);
    }

    public static void setShowLayoutChoice(Context context, boolean showLayoutChoice) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        prefs.edit()
                .putBoolean(PREFS_SHOW_LAYOUT_CHOICE, showLayoutChoice)
                .apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Set enabled state of Market preference
        PreferenceScreen sp = (PreferenceScreen) findPreference(PREFS_MARKET_EXTENSIONS);
        sp.setEnabled(isMarketAvailable());
        sp = (PreferenceScreen) findPreference(PREFS_MARKET_THEMES);
        sp.setEnabled(isMarketAvailable());

        mPrioSubtotal = (ListPreference) findPreference(PREFS_PRIOSUBTOTAL);
        mPickItemsSort = (ListPreference) findPreference(PREFS_PICKITEMS_SORTORDER);

        mIncludesChecked = (CheckBoxPreference) findPreference(PREFS_PRIOSUBINCLCHECKED);
        mExternaldb = (CheckBoxPreference) findPreference(PREFS_EXTERNALDB);

        Preference layoutChoicePreference = findPreference("layout_choice");
        layoutChoicePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(PreferenceActivity.this, LayoutChoiceActivity.class));
                return true;
            }
        });
        SharedPreferences shared = getPreferenceScreen().getSharedPreferences();
        updatePrioSubtotalSummary(shared);
        updatePickItemsSortPref(shared);
        updatePathLabel(shared);
        resetAllSettings(shared);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent() != null && getIntent().hasExtra(EXTRA_SHOW_GET_ADD_ONS)) {
            // Open License section directly:
            PreferenceScreen licensePrefScreen = (PreferenceScreen) getPreferenceScreen()
                    .findPreference(PREFS_SCREEN_ADDONS);
            setPreferenceScreen(licensePrefScreen);
        }
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        mFilterCompletionChanged = false;
    }

    @Override
    protected void onPause() {
        if (mBackupManagerAvailable) {
            new BackupManagerWrapper(this).dataChanged();
        }
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        updateCount++;
        if (key.equals(PREFS_PRIOSUBTOTAL)) {
            updatePrioSubtotalSummary(prefs);
        }
        if (key.equals(PREFS_SAMESORTFORPICK)) {
            updatePickItemsSortPref(prefs);
        }
        if (key.equals(PREFS_CURRENT_LIST_COMPLETE)) {
            mFilterCompletionChanged = true;
        }
        if (key.equals(PREFS_EXTERNALDB)) {
            final Boolean extDbSelected = prefs.getBoolean(PREFS_EXTERNALDB, PREFS_EXTERNALDB_DEFAULT);
            final String dirPath = prefs.getString(PREFS_EXTERNALDB_PATH, PREFS_EXTERNALDB_PATH_DEFAULT);
            if (extDbSelected) { // user selected external DB
                // check and request file mgr permission if needed
                if (!isFileManager()) {
                    // request MANAGE ALL FILES permission (needed b.c. Sqlite uses native code)
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, ACTIVITY_FILEMGR_PERMISSION);
                }
                // check and request file path if needed
                if (dirPath.equalsIgnoreCase(PREFS_EXTERNALDB_PATH_DEFAULT)) {
                    // Let the user choose a file path and ask for permission to access it
                    Uri initialUri = Uri.fromFile(Environment.getExternalStorageDirectory());
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri);
                    }
                    startActivityForResult(intent, ACTIVITY_CHOOSE_DB_PATH);
                }
            } else { // user selected internal DB
                final Boolean extDbUsed = prefs.getBoolean(PREFS_EXTERNALDB_USED, PREFS_EXTERNALDB_USED_DEFAULT);
                if (extDbUsed) { // if external DB is actually in use
                    setInternalDb();
                    askCopyDb(dirPath + File.separator + ShoppingDatabase.DATABASE_NAME,
                              getApplicationInfo().dataDir + File.separator + "databases" +
                              File.separator + ShoppingDatabase.DATABASE_NAME);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences prefs = getSharedPreferences(
                "org.openintents.shopping_preferences", MODE_PRIVATE);
        if (ACTIVITY_CHOOSE_DB_PATH == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    Log.d(TAG, "Received externaldb URI: " + uri);
                    // Persist the permission across device restarts
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    }
                    // Translate to file path and store in preferences
                    final String dirPath = uriToFilePath(this, uri);
                    Editor editor = prefs.edit();
                    editor.putString(PREFS_EXTERNALDB_PATH, dirPath);
                    editor.apply();
                }
            } else { // user declined request
                setInternalDb(); // revert to using internal DB
            }
        } else if (ACTIVITY_FILEMGR_PERMISSION == requestCode) {
            if (!isFileManager()) { // user declined request
                setInternalDb(); // revert to using internal DB
            }
        }
        // Any of the two activities can complete the requirements for using ext DB
        if (ACTIVITY_CHOOSE_DB_PATH == requestCode || ACTIVITY_FILEMGR_PERMISSION == requestCode) {
            final String dirPath = prefs.getString(PREFS_EXTERNALDB_PATH, PREFS_EXTERNALDB_PATH_DEFAULT);
            // if we received both the path and the file mgr permission
            if (!dirPath.equalsIgnoreCase(PREFS_EXTERNALDB_PATH_DEFAULT) && isFileManager()) {
                // Make sure this is only executed once
                final Boolean extDbUsed = prefs.getBoolean(PREFS_EXTERNALDB_USED, PREFS_EXTERNALDB_USED_DEFAULT);
                if (!extDbUsed) {
                    // update display string in prefs GUI
                    updatePathLabel(prefs);
                    askCopyDb(getApplicationInfo().dataDir + File.separator + "databases" +
                              File.separator + ShoppingDatabase.DATABASE_NAME,
                              dirPath + File.separator + ShoppingDatabase.DATABASE_NAME);
                    Editor editor = prefs.edit();
                    editor.putBoolean(PREFS_EXTERNALDB_USED, true);
                    editor.apply();
                }
            }
        }
    }

    private void setInternalDb() {
        SharedPreferences sp = getSharedPreferences(
                "org.openintents.shopping_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(PreferenceActivity.PREFS_EXTERNALDB, PREFS_EXTERNALDB_DEFAULT);
        editor.putBoolean(PreferenceActivity.PREFS_EXTERNALDB_USED, PREFS_EXTERNALDB_USED_DEFAULT);
        editor.putString(PREFS_EXTERNALDB_PATH, PREFS_EXTERNALDB_PATH_DEFAULT);
        editor.apply();
        mExternaldb.setChecked(false);
    }

    public void askCopyDb(String from, String to) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.preference_externaldb_changed);
        builder.setMessage(R.string.preference_externaldb_copy);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                File destFile = new File(to);
                if (destFile.exists()) {
                    askOverwriteDb(from, to);
                } else {
                    copyDb(from, to);
                    quit();
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                quit(); // exit app so that change takes effect
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void askOverwriteDb(String from, String to) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(android.R.string.dialog_alert_title);
        builder.setMessage(R.string.preference_externaldb_overwrite);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                copyDb(from, to);
                dialog.dismiss();
                quit();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                quit();
            }
        });
        builder.create().show();
    }

    public void copyDb(String from, String to) {
        try {
            File sourceFile = new File(from);
            FileInputStream fis = new FileInputStream(sourceFile);
            OutputStream os = new FileOutputStream(to);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = fis.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                System.exit(0);
            }
        }, 1000);
        // If killing the app without delay, pref changes do not get persisted.
    }

    /*
      This method converts a URI of the form:
      "content://com.android.externalstorage.documents/tree/primary:Documents/Shopping"
      into a file path of the form:
      "/storage/emulated/0/Documents/Shopping"
     */
    public String uriToFilePath(final Context context, final Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme()) &&
                "com.android.externalstorage.documents".equalsIgnoreCase(uri.getAuthority())) {
            Log.d(TAG, "Extracting lastPathSegment from URI: " + uri);
            final String lastPathSegment = uri.getLastPathSegment();
            final String[] split = lastPathSegment.split(":");
            final String path = split[1];
            final String result = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + path;
            return result;
        } else {
            // This is an unexpected error case
            Log.d(TAG, "Unknown type of URI: " + uri);
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    private void updatePathLabel(SharedPreferences sp) {
        final String uriStr = sp.getString(PREFS_EXTERNALDB_PATH, PREFS_EXTERNALDB_PATH_DEFAULT);
        String display;
        if (uriStr.equals(PREFS_EXTERNALDB_PATH_DEFAULT)) {
            display = getString(R.string.preference_externaldb_summary);
        } else {
            final Uri uri = Uri.parse(uriStr);
            display = uri.getPath();
        }
        mExternaldb.setSummary(display);
    }

    public boolean isFileManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        } else {
            //Below android 11
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void resetAllSettings(final SharedPreferences prefs) {
        Preference resetAllSettings = findPreference(PREFS_RESET_ALL_SETTINGS);
        resetAllSettings
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        AlertDialog alert = new AlertDialog.Builder(
                                PreferenceActivity.this).create();
                        alert.setTitle(R.string.preference_reset_all_settings);
                        alert.setMessage(getString(R.string.preference_reset_all_settings_alert));
                        alert.setButton(getString(android.R.string.yes),
                                new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        SharedPreferences.Editor editor = prefs
                                                .edit();
                                        // Main
                                        editor.putString(PREFS_FONTSIZE,
                                                PREFS_FONTSIZE_DEFAULT);
                                        editor.putString(PREFS_SORTORDER,
                                                PREFS_SORTORDER_DEFAULT);
                                        // Main advanced
                                        editor.putString(
                                                PREFS_CAPITALIZATION,
                                                String.valueOf(PREFS_CAPITALIZATION_DEFAULT));
                                        editor.putString(PREFS_ORIENTATION,
                                                PREFS_ORIENTATION_DEFAULT);
                                        editor.putBoolean(PREFS_HIDECHECKED,
                                                PREFS_HIDECHECKED_DEFAULT);
                                        editor.putBoolean(PREFS_FASTSCROLL,
                                                PREFS_FASTSCROLL_DEFAULT);
                                        editor.putBoolean(PREFS_SHAKE,
                                                PREFS_SHAKE_DEFAULT);
                                        editor.putBoolean(PREFS_PERSTOREPRICES,
                                                PREFS_PERSTOREPRICES_DEFAULT);
                                        editor.putBoolean(PREFS_ADDFORBARCODE,
                                                PREFS_ADDFORBARCODE_DEFAULT);
                                        editor.putBoolean(PREFS_SCREENLOCK,
                                                PREFS_SCREENLOCK_DEFAULT);
                                        editor.putBoolean(PREFS_SHOWINLOCKSCREEN,
                                                PREFS_SHOWINLOCKSCREEN_DEFAULT);
                                        editor.putBoolean(PREFS_EXTERNALDB,
                                                PREFS_EXTERNALDB_DEFAULT);
                                        editor.putBoolean(PREFS_EXTERNALDB_USED,
                                                PREFS_EXTERNALDB_USED_DEFAULT);
                                        editor.putString(PREFS_EXTERNALDB_PATH,
                                                PREFS_EXTERNALDB_PATH_DEFAULT);
                                        editor.putBoolean(PREFS_QUICKEDITMODE,
                                                PREFS_QUICKEDITMODE_DEFAULT);
                                        editor.putBoolean(PREFS_USE_FILTERS,
                                                PREFS_USE_FILTERS_DEFAULT);
                                        editor.putBoolean(PREFS_RESETQUANTITY,
                                                PREFS_RESETQUANTITY_DEFAULT);
                                        editor.putBoolean(PREFS_HOLO_SEARCH,
                                                PREFS_HOLO_SEARCH_DEFAULT);
                                        // Appearance
                                        editor.putBoolean(PREFS_SHOW_PRICE,
                                                PREFS_SHOW_PRICE_DEFAULT);
                                        editor.putBoolean(PREFS_SHOW_TAGS,
                                                PREFS_SHOW_TAGS_DEFAULT);
                                        editor.putBoolean(PREFS_SHOW_UNITS,
                                                PREFS_SHOW_UNITS_DEFAULT);
                                        editor.putBoolean(PREFS_SHOW_QUANTITY,
                                                PREFS_SHOW_QUANTITY_DEFAULT);
                                        editor.putBoolean(PREFS_SHOW_PRIORITY,
                                                PREFS_SHOW_PRIORITY_DEFAULT);
                                        // Pick items
                                        editor.putBoolean(
                                                PREFS_SAMESORTFORPICK,
                                                PREFS_SAMESORTFORPICK_DEFAULT);
                                        editor.putString(
                                                PREFS_PICKITEMS_SORTORDER,
                                                PREFS_PICKITEMS_SORTORDER_DEFAULT);
                                        editor.putBoolean(
                                                PREFS_PICKITEMSINLIST,
                                                PREFS_PICKITEMSINLIST_DEFAULT);
                                        editor.putString(
                                                PREFS_SORTORDER_SHOPPINGLISTS,
                                                PREFS_SORTORDER_SHOPPINGLISTS_DEFAULT);
                                        // Subtotal
                                        editor.putString(PREFS_PRIOSUBTOTAL,
                                                PREFS_PRIOSUBTOTAL_DEFAULT);
                                        editor.putBoolean(
                                                PREFS_PRIOSUBINCLCHECKED,
                                                PREFS_PRIOSUBINCLCHECKED_DEFAULT);

                                        editor.putBoolean(PREFS_CURRENT_LIST_COMPLETE, PREFS_CURRENT_LIST_COMPLETE_DEFAULT);
                                        editor.putBoolean(PREFS_SORT_PER_LIST, PREFS_SORT_PER_LIST_DEFAULT);

                                        editor.commit();

                                        Toast.makeText(
                                                PreferenceActivity.this,
                                                R.string.preference_reset_all_settings_done,
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                        );
                        alert.setButton2(getString(android.R.string.cancel),
                                new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                        alert.show();
                        return false;
                    }
                });
    }

    private void updatePrioSubtotalSummary(SharedPreferences prefs) {
        int threshold = getSubtotalByPriorityThreshold(prefs);
        CharSequence[] labels = mPrioSubtotal.getEntries();
        mPrioSubtotal.setSummary(labels[threshold]);
        mIncludesChecked.setEnabled(threshold != 0);
    }

    private void updatePickItemsSortPref(SharedPreferences prefs) {
        boolean sameSort = prefs.getBoolean(PREFS_SAMESORTFORPICK,
                PREFS_SAMESORTFORPICK_DEFAULT);
        mPickItemsSort.setEnabled(!sameSort);
        // maybe we should set the label to say the active sort order.
        // but not tonight.
        // CharSequence labels[] = mPickItemsSort.getEntries();
    }

    /**
     * Check whether Market is available.
     *
     * @return true if Market is available
     */
    private boolean isMarketAvailable() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri
                .parse(getString(R.string.preference_market_extensions_link)));
        return IntentUtils.isIntentAvailable(this, i);
    }
}
