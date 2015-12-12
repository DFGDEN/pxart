package dfgden.pxart.com.pxart.adaptors;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dfgden.pxart.com.pxart.R;
import dfgden.pxart.com.pxart.data.Author;
import dfgden.pxart.com.pxart.data.Comment;
import dfgden.pxart.com.pxart.data.Follower;
import dfgden.pxart.com.pxart.data.Following;
import dfgden.pxart.com.pxart.data.Pattern;
import dfgden.pxart.com.pxart.interfaces.ProgressUpdateDataListener;
import dfgden.pxart.com.pxart.internet.ServiceManager;
import dfgden.pxart.com.pxart.sharedpreference.PreferenceHelper;
import dfgden.pxart.com.pxart.utils.TimeUtil;


public abstract class BasePatternAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    public static final int SCALE_WALLPAPER_PARAMETER = 4;
    public static final int REDUCE_PATTERN_PARAMETER = 2;

    private OnItemRecycleViewListener onItemRecycleViewListener;
    private OnAuthorNameListener onAuthorNameListener;
    private OnFollowerListener onFollowerListener;

    private OnCurrentPositionListener onCurrentPositionListener;

    public interface OnItemRecycleViewListener {
        void onItemClick(String result);
    }

    public interface OnAuthorNameListener {
        void onNameClickListener(String author);
    }

    public interface OnCurrentPositionListener {
        void currentPosition(int pos);
    }

    public interface OnFollowerListener {
        void getFollower(Author author);

        void getFollowing(Author author);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER && onCreateHeader() != null) {
            return onCreateHeader();
        } else {
            return onCreateItem();
        }
    }

    public abstract RecyclerView.ViewHolder onCreateHeader();

    public abstract RecyclerView.ViewHolder onCreateItem();

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public abstract int getItemCount();

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public int getItem(int position) {
        return position - 1;
    }

    protected void prepareCommentItem(final CommentPattern item, final List<Comment> comments, final int position) {

        if (comments.get(getItem(position)).getAuthor().getAvatarCdnUrl() != null) {
            ImageLoader.getInstance().displayImage(comments.get(getItem(position)).getAuthor().getAvatarCdnUrl(), item.imageViewAuthor);
        } else {
            item.imageViewAuthor.setImageResource(R.drawable.logo);
        }
        item.imageViewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAuthorNameListener != null) {
                    onAuthorNameListener.onNameClickListener(comments.get(getItem(position)).getAuthor().getName());
                }
            }
        });
        item.txtAuthorComment.setText(comments.get(getItem(position)).getText());
        item.txtAuthorName.setText(comments.get(getItem(position)).getAuthor().getName());
        item.txtAuthorTimePost.setText(TimeUtil.getPubDate(comments.get(getItem(position)).getCreationTime()));

    }

    protected void prepareFollowerItem(final ArrayList<Follower> followerArrayList, FollowerPattern holder, final int position) {

        if (followerArrayList.get(position).getSubscriber().getAvatarCdnUrl() != null) {
            ImageLoader.getInstance().displayImage(followerArrayList.get(position).getSubscriber().getAvatarCdnUrl(), holder.imageViewAuthor);
        } else {
            holder.imageViewAuthor.setImageResource(R.drawable.logo);
        }
        holder.imageViewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAuthorNameListener != null) {
                    onAuthorNameListener.onNameClickListener(followerArrayList.get(position).getSubscriber().getName());
                }
            }
        });
        holder.txtAuthorName.setText(followerArrayList.get(position).getSubscriber().getName());
        holder.txtAuthorTimePost.setText(TimeUtil.getPubDate(followerArrayList.get(position).getSubscriber().getRegisteredTime()));

    }

    protected void prepareFollowingItem(final ArrayList<Following> followerArrayList, FollowerPattern holder, final int position) {
        if (followerArrayList.get(position).getPublisher().getAvatarCdnUrl() != null) {
            ImageLoader.getInstance().displayImage(followerArrayList.get(position).getPublisher().getAvatarCdnUrl(), holder.imageViewAuthor);
        } else {
            holder.imageViewAuthor.setImageResource(R.drawable.logo);
        }
        holder.imageViewAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAuthorNameListener != null) {
                    onAuthorNameListener.onNameClickListener(followerArrayList.get(position).getPublisher().getName());
                }
            }
        });
        holder.txtAuthorName.setText(followerArrayList.get(position).getPublisher().getName());
        holder.txtAuthorTimePost.setText(TimeUtil.getPubDate(followerArrayList.get(position).getPublisher().getRegisteredTime()));

    }

    protected void prepareUserHeader(final UserPattern header, final Author author, final Activity activity) {
        if (author.getAvatarCdnUrl() == null) {
            header.imageViewAuthor.setImageResource(R.drawable.logo);
        } else {
            ImageLoader.getInstance().displayImage(author.getAvatarCdnUrl(), header.imageViewAuthor);
        }
        header.txtAuthorName.setText(author.getName());
        header.txtAuthorTimeReg.setText(TimeUtil.getPubDate(author.getRegisteredTime()));
        header.txtCountFollower.setText(String.valueOf(author.getFollowersCount()));
        header.txtCountFollowing.setText(String.valueOf(author.getFollowingCount()));

        header.btnLayoutFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFollowerListener != null) {
                    onFollowerListener.getFollower(author);
                }

            }
        });

        header.btnLayoutFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFollowerListener != null) {
                    onFollowerListener.getFollowing(author);

                }
            }
        });

        header.btnSubscribe.setText(author.isIsFollowed() ? R.string.basepatternadaptor_upsubscribe : R.string.basepatternadaptor_subscribe);
        if (!PreferenceHelper.getInstance().token.equals("")) {
            header.btnSubscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (author.isIsFollowed()) {
                        author.setIsFollowed(false);

                        ServiceManager.getInstance().deleteFolowers(author.getName(), new ProgressUpdateDataListener() {
                            @Override
                            public void startUpdate() {

                            }
                            @Override
                            public void stopUpdate() {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        header.btnSubscribe.setText(R.string.basepatternadaptor_subscribe);
                                        author.setFollowersCount(author.getFollowersCount() - 1);
                                        notifyDataSetChanged();
                                    }
                                });
                            }

                            @Override
                            public void crash(final String text) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        author.setIsFollowed(true);
                                        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    } else {
                        author.setIsFollowed(true);
                        ServiceManager.getInstance().putFolowers(author.getName(), new ProgressUpdateDataListener() {
                            @Override
                            public void startUpdate() {

                            }

                            @Override
                            public void stopUpdate() {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        header.btnSubscribe.setText(R.string.basepatternadaptor_upsubscribe);
                                        author.setFollowersCount(author.getFollowersCount() + 1);
                                        notifyDataSetChanged();
                                    }
                                });
                            }

                            @Override
                            public void crash(final String text) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        author.setIsFollowed(false);
                                        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }
                }
            });

        }
    }

    protected boolean isClickableAvaBaseItem() {
        return false;
    }

    public void getCurrentItemPosition(OnCurrentPositionListener onCurrentPositionListener) {
            this.onCurrentPositionListener = onCurrentPositionListener;
    }

    protected void prepareBaseItem(final BasePattern header,final ArrayList<Pattern> patterns,final int position, final Activity activity) {
        if (onCurrentPositionListener !=null){
            onCurrentPositionListener.currentPosition(position);
        }
        if (patterns.get(position).getAuthor() == null || patterns.get(position).getAuthor().getAvatarCdnUrl() == null) {
            header.imageViewAuthor.setImageResource(R.drawable.logo);
        } else {
            ImageLoader.getInstance().displayImage(patterns.get(position).getAuthor().getAvatarCdnUrl(), header.imageViewAuthor);
        }
        if (isClickableAvaBaseItem() && patterns.get(position).getAuthor() != null && patterns.get(position).getAuthor().getName() != null) {
            header.imageViewAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAuthorNameListener != null) {
                        onAuthorNameListener.onNameClickListener(patterns.get(position).getAuthor().getName() == null ? "" : patterns.get(position).getAuthor().getName());
                    }
                }
            });
        }

        header.txtAuthorName.setText(patterns.get(position).getAuthor() == null || patterns.get(position).getAuthor().getName() == null ? activity.getString(R.string.basepatternadaptor_authornoname) : patterns.get(position).getAuthor().getName());
        header.txtPatternName.setText(!patterns.get(position).getName().isEmpty() ? patterns.get(position).getName() : activity.getString(R.string.basepatternadaptor_patternnoname));
        header.txtViewPatternLikes.setText(String.valueOf(patterns.get(position).getLikesCount()));
        header.txtViewPatternComments.setText(String.valueOf(patterns.get(position).getCommentsCount()));
        header.txtAuthorTimePost.setText(TimeUtil.getPubDate(patterns.get(position).getCreationTime()));
        header.imgButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupMenu(activity, v, patterns.get(position));

            }
        });
        header.imgLike.setImageResource(patterns.get(position).isLiked() ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
        if (!PreferenceHelper.getInstance().token.isEmpty()) {
            header.layoutLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (patterns.get(position).isLiked()) {
                        patterns.get(position).setLiked(false);
                        ServiceManager.getInstance().deleteLike(patterns.get(position), new ProgressUpdateDataListener() {
                            @Override
                            public void startUpdate() {

                            }

                            @Override
                            public void stopUpdate() {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        patterns.get(position).setLikesCount(patterns.get(position).getLikesCount() - 1);
                                        notifyDataSetChanged();
                                    }
                                });

                            }

                            @Override
                            public void crash(final String text) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        patterns.get(position).setLiked(true);
                                        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                });

                            }
                        });
                    } else {
                        patterns.get(position).setLiked(true);
                        ServiceManager.getInstance().putLike(patterns.get(position), new ProgressUpdateDataListener() {
                            @Override
                            public void startUpdate() {

                            }

                            @Override
                            public void stopUpdate() {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        patterns.get(position).setLikesCount(patterns.get(position).getLikesCount() + 1);
                                        notifyDataSetChanged();

                                    }
                                });
                            }

                            @Override
                            public void crash(final String text) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        patterns.get(position).setLiked(false);
                                        patterns.get(position).setLikesCount(patterns.get(position).getLikesCount() + 1);
                                        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();

                                    }
                                });
                            }
                        });
                    }
                }

            });
        }

        ImageLoader.getInstance().loadImage(patterns.get(position).getImageCdnUrl(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                header.patternLayout.setBackgroundDrawable(createBitmapDrawable(loadedImage, activity));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        header.adaptorContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRecycleViewListener != null) {
                    onItemRecycleViewListener.onItemClick(patterns.get(position).getId());
                }
            }
        });
    }

    private BitmapDrawable createBitmapDrawable(Bitmap image,Context context) {
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        Bitmap resultBitmap = Bitmap.createScaledBitmap(image, image.getWidth() *(displaymetrics.densityDpi/160)/ REDUCE_PATTERN_PARAMETER, image.getHeight() *(displaymetrics.densityDpi/160)/ REDUCE_PATTERN_PARAMETER, false) ;
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resultBitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return bitmapDrawable;
    }

    public void setOnItemRecycleViewClickListener(OnItemRecycleViewListener listener) {
        onItemRecycleViewListener = listener;
    }

    private void createPopupMenu(final Activity activity, View v, final Pattern pattern) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.inflate(R.menu.popupmenu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuWallpaper:

                        ImageLoader.getInstance().loadImage(pattern.getImageCdnUrl(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                installWallpapers(loadedImage, activity);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
                        break;
//                    case R.id.menuChange:
//                        ImageLoader.getInstance().loadImage(pattern.getImageCdnUrl(), new ImageLoadingListener() {
//                            @Override
//                            public void onLoadingStarted(String imageUri, View view) {
//
//                            }
//
//                            @Override
//                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                            }
//
//                            @Override
//                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                Intent intent = new Intent(activity, PaintActivity.class);
//                                intent.putExtra(PaintActivity.CURRENT_PATTERN, loadedImage);
//                                activity.startActivityForResult(intent, MainActivity.REQUEST_CODE1);
//                            }
//
//                            @Override
//                            public void onLoadingCancelled(String imageUri, View view) {
//
//                            }
//                        });
//
//                        break;

                }

                return true;
            }
        });

    }

    private void installWallpapers(Bitmap image, Context context) {
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        Bitmap b = Bitmap.createBitmap(displaymetrics.widthPixels/(displaymetrics.densityDpi /160)* SCALE_WALLPAPER_PARAMETER, displaymetrics.heightPixels/(displaymetrics.densityDpi /160)* SCALE_WALLPAPER_PARAMETER, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image,image.getWidth()*(displaymetrics.densityDpi /160),image.getHeight()*(displaymetrics.densityDpi /160),false);
        for (int i = 0; i < displaymetrics.widthPixels/(displaymetrics.densityDpi /160)* SCALE_WALLPAPER_PARAMETER; i += scaledBitmap.getWidth()) {
            for (int j = 0; j < displaymetrics.heightPixels/(displaymetrics.densityDpi /160)* SCALE_WALLPAPER_PARAMETER; j += scaledBitmap.getHeight()) {
                c.drawBitmap(scaledBitmap, i, j, p);
            }
        }
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);

        try {
            myWallpaperManager.setBitmap(b);
        } catch (IOException e) {
            Toast.makeText(context, R.string.basepatternadaptor_wallpapererror, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void setOnAuthorNameClickListener(OnAuthorNameListener onAuthorNameListener) {
        this.onAuthorNameListener = onAuthorNameListener;
    }

    public void setOnFollowerClickListener(OnFollowerListener onFollowerListener) {
        this.onFollowerListener = onFollowerListener;
    }

    protected class BasePattern extends RecyclerView.ViewHolder {
        private ImageView imageViewAuthor, imgLike;
        private ImageButton imgButtonMenu;
        private TextView txtViewPatternLikes, txtViewPatternComments, txtAuthorName, txtAuthorTimePost, txtPatternName;
        private LinearLayout patternLayout, adaptorContainer, layoutLike;

        public BasePattern(View itemView) {
            super(itemView);
            layoutLike = (LinearLayout) itemView.findViewById(R.id.layoutLike);
            imageViewAuthor = (ImageView) itemView.findViewById(R.id.imageViewAuthor);
            imgButtonMenu = (ImageButton) itemView.findViewById(R.id.imgButtonMenu);
            imgButtonMenu.setVisibility(View.VISIBLE);
            txtViewPatternLikes = (TextView) itemView.findViewById(R.id.txtViewPatternLikes);
            txtViewPatternComments = (TextView) itemView.findViewById(R.id.txtViewPatternComments);
            txtAuthorName = (TextView) itemView.findViewById(R.id.txtAuthorName);
            patternLayout = (LinearLayout) itemView.findViewById(R.id.patternLayout);
            adaptorContainer = (LinearLayout) itemView.findViewById(R.id.adaptorContainer);
            txtAuthorTimePost = (TextView) itemView.findViewById(R.id.txtAuthorTimePost);
            txtPatternName = (TextView) itemView.findViewById(R.id.txtPatternName);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
        }
    }

    protected class CommentPattern extends RecyclerView.ViewHolder {
        private TextView txtAuthorTimePost, txtAuthorComment, txtAuthorName;
        private ImageView imageViewAuthor;

        public CommentPattern(View itemView) {
            super(itemView);
            txtAuthorTimePost = (TextView) itemView.findViewById(R.id.txtAuthorTimePost);
            txtAuthorComment = (TextView) itemView.findViewById(R.id.txtPatternName);
            txtAuthorName = (TextView) itemView.findViewById(R.id.txtAuthorName);
            imageViewAuthor = (ImageView) itemView.findViewById(R.id.imageViewAuthor);
        }
    }

    protected class FollowerPattern extends RecyclerView.ViewHolder {

        private TextView txtAuthorTimePost, txtAuthorComment, txtAuthorName;
        private ImageView imageViewAuthor;

        public FollowerPattern(View itemView) {
            super(itemView);
            txtAuthorTimePost = (TextView) itemView.findViewById(R.id.txtAuthorTimePost);
            txtAuthorComment = (TextView) itemView.findViewById(R.id.txtPatternName);
            txtAuthorComment.setVisibility(View.INVISIBLE);
            txtAuthorName = (TextView) itemView.findViewById(R.id.txtAuthorName);
            imageViewAuthor = (ImageView) itemView.findViewById(R.id.imageViewAuthor);
        }
    }

    protected class UserPattern extends RecyclerView.ViewHolder {

        private ImageView imageViewAuthor;
        private TextView txtAuthorName, txtAuthorTimeReg, txtCountFollower, txtCountFollowing;
        private Button btnSubscribe;
        private LinearLayout btnLayoutFollower, btnLayoutFollowing;

        public UserPattern(View itemView) {
            super(itemView);
            imageViewAuthor = (ImageView) itemView.findViewById(R.id.imageViewAuthor);
            txtAuthorName = (TextView) itemView.findViewById(R.id.txtAuthorName);
            txtAuthorTimeReg = (TextView) itemView.findViewById(R.id.txtAuthorTimeReg);
            btnSubscribe = (Button) itemView.findViewById(R.id.btnSubscribe);
            txtCountFollower = (TextView) itemView.findViewById(R.id.txtCountFollower);
            txtCountFollowing = (TextView) itemView.findViewById(R.id.txtCountFollowing);
            btnLayoutFollower = (LinearLayout) itemView.findViewById(R.id.btnLayoutFollower);
            btnLayoutFollowing = (LinearLayout) itemView.findViewById(R.id.btnLayoutFollowing);
        }
    }
}


