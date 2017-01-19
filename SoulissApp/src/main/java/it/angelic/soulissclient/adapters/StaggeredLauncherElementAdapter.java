package it.angelic.soulissclient.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import it.angelic.soulissclient.Constants;
import it.angelic.soulissclient.NodesListActivity;
import it.angelic.soulissclient.ProgramListActivity;
import it.angelic.soulissclient.R;
import it.angelic.soulissclient.SceneListActivity;
import it.angelic.soulissclient.SoulissApp;
import it.angelic.soulissclient.TagDetailActivity;
import it.angelic.soulissclient.TypicalDetailFragWrapper;
import it.angelic.soulissclient.fragments.TagDetailFragment;
import it.angelic.soulissclient.model.ISoulissCommand;
import it.angelic.soulissclient.model.LauncherElement;
import it.angelic.soulissclient.model.LauncherElementEnum;
import it.angelic.soulissclient.model.SoulissTag;
import it.angelic.soulissclient.model.SoulissTypical;
import it.angelic.soulissclient.util.FontAwesomeUtil;
import it.angelic.soulissclient.util.SoulissUtils;

public class StaggeredLauncherElementAdapter extends RecyclerView.Adapter<StaggeredLauncherElementAdapter.ViewHolder> {

    private Activity context;
    private List<LauncherElement> launcherElements;

    public StaggeredLauncherElementAdapter(Activity context, List<LauncherElement> launcherElements) {
        this.launcherElements = launcherElements;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return launcherElements.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return launcherElements.get(position).getComponentEnum().ordinal();
    }

    public List getLauncherElements() {
        return launcherElements;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LauncherElement item = launcherElements.get(position);

        //holder.container.removeAllViews();
        //holder.textView.setText(item.title);
        // holder.container = launcherElements[position].inflateCardView();

        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
            sglp.setFullSpan(item.isFullSpan());
            Log.w(Constants.TAG, "Full span for element?" + holder.getItemViewType());
            holder.itemView.setLayoutParams(sglp);
        }
        //qui la view c'e` gia
        switch (item.getComponentEnum()) {
            case STATIC_SCENES:
                View viewLine = holder.container.findViewById(R.id.StaticTileLine);
                TextView txtTit = (TextView) holder.container.findViewById(R.id.card_static_title);
                TextView txtDesc = (TextView) holder.container.findViewById(R.id.card_static_desc);
                TextView txtAwesom = (TextView) holder.container.findViewById(R.id.card_thumbnail_fa);
                FontAwesomeUtil.prepareFontAweTextView(context, txtAwesom, "fa-moon-o");
                txtTit.setText(context.getString(R.string.scenes_title));
                txtDesc.setText(item.getDesc());
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent nodeDatail = new Intent(SoulissApp.getAppContext(), SceneListActivity.class);
                        context.startActivity(nodeDatail);
                    }


                });
                viewLine.setBackgroundColor(context.getResources().getColor(R.color.std_yellow));
                break;
            case STATIC_MANUAL:
                View viewLine2 = holder.container.findViewById(R.id.StaticTileLine);
                TextView txtTit2 = (TextView) holder.container.findViewById(R.id.card_static_title);
                TextView txtDesc2 = (TextView) holder.container.findViewById(R.id.card_static_desc);
                TextView txtAwesom2 = (TextView) holder.container.findViewById(R.id.card_thumbnail_fa);
                FontAwesomeUtil.prepareFontAweTextView(context, txtAwesom2, "fa-codepen");
                txtTit2.setText(context.getString(R.string.manual_title));
                txtDesc2.setText(item.getDesc());
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent nodeDatail = new Intent(SoulissApp.getAppContext(), NodesListActivity.class);
                        context.startActivity(nodeDatail);
                    }


                });
                viewLine2.setBackgroundColor(context.getResources().getColor(R.color.std_green));
                break;
            case STATIC_PROGRAMS:
                View viewLine3 = holder.container.findViewById(R.id.StaticTileLine);
                TextView txtTit3 = (TextView) holder.container.findViewById(R.id.card_static_title);
                TextView txtDesc3 = (TextView) holder.container.findViewById(R.id.card_static_desc);
                TextView txtAwesom3 = (TextView) holder.container.findViewById(R.id.card_thumbnail_fa);
                FontAwesomeUtil.prepareFontAweTextView(context, txtAwesom3, "fa-calendar");
                txtTit3.setText(context.getString(R.string.programs_title));
                txtDesc3.setText(item.getDesc());
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent nodeDatail = new Intent(SoulissApp.getAppContext(), ProgramListActivity.class);
                        context.startActivity(nodeDatail);
                    }


                });
                viewLine3.setBackgroundColor(context.getResources().getColor(R.color.std_blue));
                break;
            case TYPICAL:

                final SoulissTypical tipico = (SoulissTypical) item.getLinkedObject();
                Log.d(Constants.TAG, "Element " + position + " set: last upd: " + SoulissUtils.getTimeAgo(tipico.getTypicalDTO().getRefreshedAt()));

                TextView textView = (TextView) holder.container.findViewById(R.id.TextViewTypicalsTitle);
                TextView imageView = (TextView) holder.container.findViewById(R.id.card_thumbnail_image2);
                LinearLayout linearActionsLayout = (LinearLayout) holder.container.findViewById(R.id.linearLayoutButtons);
                TextView textViewInfo1 = (TextView) holder.container.findViewById(R.id.TextViewInfoStatus);
                TextView textViewInfo2 = (TextView) holder.container.findViewById(R.id.TextViewInfo2);

                textView.setText(tipico.getNiceName());
                textView.setTag(position);
                tipico.setOutputDescView(textViewInfo1);
                textViewInfo2.setText(SoulissUtils.getTimeAgo(tipico.getTypicalDTO().getRefreshedAt()));
                // imageView.setImageResource(FontAwesomeUtil.remapIconResId(tipico.getIconResourceId()));
                FontAwesomeUtil.prepareFontAweTextView(context, imageView, FontAwesomeUtil.remapIconResId(tipico.getIconResourceId()));
                linearActionsLayout.removeAllViews();
                //tipico.getActionsLayout(SoulissApp.getAppContext(), linearActionsLayout);

                List<ISoulissCommand> pi = tipico.getCommands(context);
                for (final ISoulissCommand cmd : pi) {
                    //  LinearLayout view = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.button_flat, null);
                    // or LinearLayout buttonView = (LinearLayout)this.getLayoutInflater().inflate(R.layout.my_button, null);
                    Button myButton = (AppCompatButton) LayoutInflater.from(context).inflate(R.layout.button_flat, linearActionsLayout, false);
                    //myButton.setId(myButton.getId());
                    myButton.setText(cmd.getName());
                    // view.removeView(myButton);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cmd.execute();
                        }
                    });
                    linearActionsLayout.addView(myButton);
                    linearActionsLayout.forceLayout();
                    //context.getLayoutInflater().inflate(R.layout.button_flat, linearActionsLayout);
                }
                //linearActionsLayout.removeAllViews();
                // LinearLayout ll = (LinearLayout)context.getLayoutInflater().inflate(R.layout.button_flat, linearActionsLayout);
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.w(Constants.TAG, "Activating TYP" + tipico.getNiceName());
                        Intent nodeDatail = new Intent(SoulissApp.getAppContext(), TypicalDetailFragWrapper.class);
                        // TagRecyclerAdapter.TagViewHolder holder = ( TagRecyclerAdapter.TagViewHolder holder) view;
                        nodeDatail.putExtra("TIPICO", tipico);
                        context.startActivity(nodeDatail);
                    }


                });

                break;
            case TAG:
                final SoulissTag soulissTag = (SoulissTag) item.getLinkedObject();
                TextView textCmd = (TextView) holder.container.findViewById(R.id.TextViewTagTitle);
                TextView textCmdWhen = (TextView) holder.container.findViewById(R.id.TextViewTagDesc);
                final ImageView image = (ImageView) holder.container.findViewById(R.id.imageViewTag);
                final TextView imageTag = (TextView) holder.container.findViewById(R.id.imageTagIconFa);
                final ImageView shadowbar = (ImageView) holder.container.findViewById(R.id.infoTagAlpha);
                String quantityString = context.getResources().getQuantityString(R.plurals.Devices,
                        0);
                try {
                    List<SoulissTypical> appoggio = soulissTag.getAssignedTypicals();
                    quantityString = context.getResources().getQuantityString(R.plurals.Devices,
                            appoggio.size(), appoggio.size());
                } catch (Exception ce) {
                    Log.w(Constants.TAG, "TAG Empty? ");
                }
                textCmd.setText(soulissTag.getName());
                textCmdWhen.setText(quantityString);
                if (soulissTag.getIconResourceId() != 0) {
                    FontAwesomeUtil.prepareFontAweTextView(context, imageTag, FontAwesomeUtil.remapIconResId(soulissTag.getIconResourceId()));
                    imageTag.setVisibility(View.VISIBLE);
                } else {
                    FontAwesomeUtil.prepareFontAweTextView(context, imageTag, "fa-window");
                    imageTag.setVisibility(View.INVISIBLE);
                }
                // Here you apply the animation when the view is bound
                //setAnimation(holder.container, position);

                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.w(Constants.TAG, "Activating TAG " + soulissTag.getNiceName());
                        Intent nodeDatail = new Intent(SoulissApp.getAppContext(), TagDetailActivity.class);
                        // TagRecyclerAdapter.TagViewHolder holder = ( TagRecyclerAdapter.TagViewHolder holder) view;
                        nodeDatail.putExtra("TAG", soulissTag.getTagId());

                        ActivityOptionsCompat options =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                                        //holder.image,   // The view which starts the transition
                                        //"photo_hero"    // The transitionName of the view we’re transitioning to
                                        Pair.create((View) image, "photo_hero"),
                                        Pair.create((View) shadowbar, "shadow_hero"),
                                        Pair.create((View) imageTag, "tag_icon")
                                );

                        ActivityCompat.startActivity(context, nodeDatail, options.toBundle());
                    }


                });


                holder.container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;//chiama Parentàs onItemClickListener
                    }
                });

                //holder.image.setImageResource(soulissTags[position].getIconResourceId());
                try {
                    File picture = new File(TagDetailFragment.getRealPathFromURI(Uri.parse(soulissTag.getImagePath())));

                    // File picture = new File(Uri.parse(collectedTag.getImagePath()).getPath());
                    if (picture.exists()) {
                        //ImageView imageView = (ImageView)findViewById(R.id.imageView);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        options.inPreferQualityOverSpeed = false;
                        Bitmap myBitmap = BitmapFactory.decodeFile(picture.getAbsolutePath(), options);
                        Log.i(Constants.TAG, "bitmap size " + myBitmap.getRowBytes());
                        image.setImageBitmap(myBitmap);
                    }

                } catch (Exception io) {
                    Log.i(Constants.TAG, "cant load image " + soulissTag.getImagePath());
                    image.setImageDrawable(ContextCompat.getDrawable(SoulissApp.getAppContext(), R.drawable.home_automation));
                }
                break;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LauncherElementEnum enumVal = LauncherElementEnum.values()[viewType];
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.cardview_launcher2, parent, false);
        switch (enumVal) {

            case STATIC_SCENES:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.card_button_static, parent, false);
                break;
            case STATIC_PROGRAMS:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.card_button_static, parent, false);
                break;
            case STATIC_MANUAL:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.card_button_static, parent, false);
                break;
            case TYPICAL:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.cardview_typical_vertical, parent, false);
                break;
            case TAG:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.cardview_tag, parent, false);
                break;


        }
        return new ViewHolder(itemView);
    }

    public void removeAt(int position) {
        launcherElements.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, launcherElements.size());
    }

    public void setLauncherElements(List<LauncherElement> in) {
        launcherElements = in;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.getRootView();
        }
        //enum componentEnum
    }


}