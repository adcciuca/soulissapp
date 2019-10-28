package it.angelic.soulissclient.fragments.typicals;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import it.angelic.soulissclient.AbstractStatusedFragmentActivity;
import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.R;
import it.angelic.soulissclient.SoulissApp;
import it.angelic.soulissclient.TagDetailActivity;
import it.angelic.soulissclient.helpers.AlertDialogHelper;
import it.angelic.soulissclient.helpers.SoulissPreferenceHelper;
import it.angelic.soulissclient.model.SoulissTag;
import it.angelic.soulissclient.model.SoulissTypical;
import it.angelic.soulissclient.model.db.SoulissDBTagHelper;
import it.angelic.tagviewlib.OnSimpleTagClickListener;
import it.angelic.tagviewlib.OnSimpleTagDeleteListener;
import it.angelic.tagviewlib.SimpleTagRelativeLayout;
import it.angelic.tagviewlib.SimpleTagView;

public class AbstractTypicalFragment extends Fragment {
    protected TableRow infoTags;
    protected SimpleTagRelativeLayout tagView;
    protected SoulissPreferenceHelper opzioni;
    protected SoulissTypical collected;
    Toolbar actionBar;

    public AbstractTypicalFragment() {
        super();
        opzioni = SoulissApp.getOpzioni();
    }

    public SoulissTypical getCollected() {
        return collected;
    }

    void setCollected(SoulissTypical collected) {
        this.collected = collected;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Rinomina nodo e scelta icona
        inflater.inflate(R.menu.addto_ctx_menu, menu);
        Log.i(Constants.TAG, "Inflated Equalizer menu");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.addTo:
                SoulissDBTagHelper dbt = new SoulissDBTagHelper(getActivity());
                AlertDialog.Builder alert4 = AlertDialogHelper.addToCommandDialog(getActivity(), dbt, collected, null, null);
                AlertDialog built = alert4.create();
                built.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                built.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        actionBar = getActivity().findViewById(R.id.my_awesome_toolbar);

        ((AbstractStatusedFragmentActivity) getActivity()).setSupportActionBar(actionBar);
        ((AbstractStatusedFragmentActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshStatusIcon();
    }

    protected void refreshStatusIcon() {
        try {
            View ds = actionBar.getRootView();
            if (ds != null) {
                //TextView info1 = (TextView) ds.findViewById(R.id.TextViewInfoStatus);
                //  TextView info2 = (TextView) ds.findViewById(R.id.TextViewInfo2);
                ImageButton online = ds.findViewById(R.id.online_status_icon);
                TextView statusOnline = ds.findViewById(R.id.online_status);

                TextView actionTitle = ds.findViewById(R.id.actionbar_title);
                if (collected != null) {
                    actionTitle.setText(collected.getNiceName());
                }
                if (!opzioni.isSoulissReachable()) {
                    online.setBackgroundResource(R.drawable.red);
                    statusOnline.setTextColor(ContextCompat.getColor(getContext(), R.color.std_red));
                    statusOnline.setText(R.string.offline);
                } else {
                    online.setBackgroundResource(R.drawable.green);
                    statusOnline.setTextColor(ContextCompat.getColor(getContext(), R.color.std_green));
                    statusOnline.setText(R.string.Online);
                }
                statusOnline.invalidate();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "FAIL refresh status icon: " + e.getMessage());
        }
    }

    protected void refreshTagsInfo() {
        tagView.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (collected.getTypicalDTO().isTagged() || collected.getTypicalDTO().isFavourite()) {
                        SoulissDBTagHelper tagDb = new SoulissDBTagHelper(getContext());
                        List<SoulissTag> tags = tagDb.getTagsByTypicals(collected);
                        tagView.removeAll();
                        StringBuilder tagInfo = new StringBuilder();
                        tagInfo.append(getString(R.string.amongTags)).append("\n");
                        for (SoulissTag newT : tags) {
                            SimpleTagView nuovoTag = new SimpleTagView(getContext(), newT.getNiceName());

                            //nuovoTag.setColor(ContextCompat.getColor(getContext(), R.color.black_overlay));
                            nuovoTag.setFontAwesome("fa-tag");
                            nuovoTag.setDeletable(true);
                            Log.w(Constants.TAG, "adding tag to view: " + nuovoTag.getText());
                            tagView.addTag(nuovoTag);
                            //badgeView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.grey_alpha));
                            //tagInfo.append("-").append(newT.getNiceName()).append("\n");
                        }
                        if (infoTags != null)
                            infoTags.setVisibility(View.VISIBLE);
                        //textviewHistoryTags.setText(tagInfo.toString());
                    } else {
                        if (infoTags != null)
                            infoTags.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e(Constants.TAG, "FAIL refreshTagsInfo: " + e.getMessage());
                }

                if (tagView != null) {
                    tagView.setOnSimpleTagDeleteListener(new OnSimpleTagDeleteListener() {
                        @Override
                        public void onTagDeleted(SimpleTagView tag) {
                            final SoulissDBTagHelper tagDb = new SoulissDBTagHelper(getContext());

                            List<SoulissTag> toRemoveL = tagDb.getTagsByTypicals(collected);
                            for (SoulissTag tr : toRemoveL) {
                                if (tr.getName().equals(tag.getText())) {
                                    Log.w(Constants.TAG, "Removing " + tr.getName() + " from " + collected.toString());
                                    List<SoulissTypical> temp = tr.getAssignedTypicals();
                                    //necessaria sta roba perche non ce equals?
                                    //no, perche il DB fa da se. Ci sarebbe considerazione
                                    //filosofica sul fatto che in realta equals e` quella,
                                    //e in questo caso la terna nodeid:slot:tagId
                                    tagDb.deleteTagTypicalNode(collected, tr);
                                    Log.w(Constants.TAG, "Removed TAG from typical");
                                }
                            }
                            tagView.remove(tag);
                            //Toast.makeText(MainActivity.this, "\"" + tag.text + "\" deleted", Toast.LENGTH_SHORT).show();
                        }

                    });
                    tagView.setOnSimpleTagClickListener(new OnSimpleTagClickListener() {
                        @Override
                        public void onSimpleTagClick(SimpleTagView tag) {
                            final SoulissDBTagHelper tagDb = new SoulissDBTagHelper(getContext());

                            Intent myIntent = new Intent(getActivity(), TagDetailActivity.class);
                            List<SoulissTag> toRemoveL = tagDb.getTagsByTypicals(collected);
                            for (SoulissTag tr : toRemoveL) {
                                if (tr.getName().equals(tag.getText())) {
                                    Log.w(Constants.TAG, "Selected " + tr.getName() + " from " + collected.toString());
                                    myIntent.putExtra("TAG", tr.getTagId());

                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    getActivity().startActivity(myIntent);
                                }
                            }

                        }
                    });
                }
            }
        }, 500);//con calma
    }

    /**
     * Siccome nel rientro da dettaglio nested a dettaglio
     * gli elementi non sono ancora presenti, si postpone la transazione per sbloccarla
     * poi con una chiamata a codesto metodo
     */
    protected void scheduleStartPostponedTransition(final View sharedElement) {
        Log.w(Constants.TAG, "SCHEDULE  ");
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        Log.w(Constants.TAG, "SCHEDULE StartPostponedTransition ");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getActivity().startPostponedEnterTransition();
                        }
                        return true;
                    }
                });
    }
}