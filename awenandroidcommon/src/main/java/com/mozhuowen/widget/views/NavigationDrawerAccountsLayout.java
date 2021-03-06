package com.mozhuowen.widget.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blunderer.materialdesignlibrary.listeners.OnMoreAccountClickListener;
import com.blunderer.materialdesignlibrary.models.Account;
import com.blunderer.materialdesignlibrary.models.NavigationDrawerAccountsListItemAccount;
import com.blunderer.materialdesignlibrary.views.RoundedImageView;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mozhuowen.R;

/**
 * 重构 com.blunderer.materialdesignlibrary.views.NavigationDrawerAccountsLayout
 */
public class NavigationDrawerAccountsLayout extends ANavigationDrawerAccountsLayout {

    private RoundedImageView mSecondPicture;
    private RoundedImageView mThirdPicture;
    private ProgressBar mSecondPictureProgressBar;
    private ProgressBar mThirdPictureProgressBar;

    public NavigationDrawerAccountsLayout(Context context) {
        this(context, null);
    }

    public NavigationDrawerAccountsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(R.layout.mdl_navigation_drawer_accounts);
    }

    @Override
    protected void changeAccount() {
        super.changeAccount();

        Account secondAccount = getAccount(1);
        if (secondAccount == null) return;
        if (secondAccount.usePictureUrl()) {
            secondAccount.getPictureUrl().into(new SimpleTarget<GlideDrawable>() {

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    mSecondPicture.setImageDrawable(resource);
                    mSecondPictureProgressBar.setVisibility(INVISIBLE);
                }

            });
        } else mSecondPicture.setImageDrawable(secondAccount.getPictureResource());

        Account thirdAccount = getAccount(2);
        if (thirdAccount == null) return;
        if (thirdAccount.usePictureUrl()) {
            thirdAccount.getPictureUrl().into(new SimpleTarget<GlideDrawable>() {

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    mThirdPicture.setImageDrawable(resource);
                    mThirdPictureProgressBar.setVisibility(INVISIBLE);
                }

            });
        } else mThirdPicture.setImageDrawable(thirdAccount.getPictureResource());
    }

    /**自定义的触发TOPVIEW方法*/
    @Override
    protected void onClickAccountsLayout() {
        if (onClickAccountsLayoutDelegate != null)
            onClickAccountsLayoutDelegate.onClick();
    }

    @Override
    protected void initAccounts() {
        ViewGroup dataLayout = (ViewGroup) mMainLayout.getChildAt(1);
        ViewGroup dataPicturesLayout = (ViewGroup) dataLayout.getChildAt(0);
        ViewGroup dataTextViewsLayout = (ViewGroup) ((ViewGroup) dataLayout.getChildAt(1))
                .getChildAt(0);
        mBackground = (ImageView) mMainLayout.getChildAt(0);
        mPicture = (RoundedImageView) dataPicturesLayout.getChildAt(3);
        mPictureProgressBar = (ProgressBar) dataPicturesLayout.getChildAt(0);
        mTitle = (TextView) dataTextViewsLayout.getChildAt(0);
        mDescription = (TextView) dataTextViewsLayout.getChildAt(1);

        if (getAccount(1) != null) {
            mSecondPicture = (RoundedImageView) dataPicturesLayout.getChildAt(5);
            mSecondPicture.setVisibility(VISIBLE);
            mSecondPicture.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int tmpPosition = mAccountsPositions[0];
                    mAccountsPositions[0] = mAccountsPositions[1];
                    if (getAccount(2) == null) mAccountsPositions[1] = tmpPosition;
                    else {
                        mAccountsPositions[1] = mAccountsPositions[2];
                        mAccountsPositions[2] = tmpPosition;
                    }
                    changeAccount();
                    if (mOnAccountChangeListener != null) {
                        mOnAccountChangeListener
                                .onAccountChange(mAccounts.get(mAccountsPositions[0]));
                    }
                }

            });
            mSecondPictureProgressBar = (ProgressBar) dataPicturesLayout.getChildAt(2);
            mSecondPictureProgressBar.setVisibility(VISIBLE);
        }

        if (getAccount(2) != null) {
            mThirdPicture = (RoundedImageView) dataPicturesLayout.getChildAt(4);
            mThirdPicture.setVisibility(VISIBLE);
            mThirdPicture.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int tmpPosition = mAccountsPositions[0];
                    mAccountsPositions[0] = mAccountsPositions[2];
                    mAccountsPositions[2] = tmpPosition;
                    changeAccount();
                    if (mOnAccountChangeListener != null) {
                        mOnAccountChangeListener
                                .onAccountChange(mAccounts.get(mAccountsPositions[0]));
                    }
                }

            });
            mThirdPictureProgressBar = (ProgressBar) dataPicturesLayout.getChildAt(1);
            mThirdPictureProgressBar.setVisibility(VISIBLE);
        }

        changeAccount();
    }

    @Override
    protected void initAccountsMenuSwitch() {
        ViewGroup switchLayout = (ViewGroup) ((ViewGroup) mMainLayout.getChildAt(1)).getChildAt(1);
        mAccountsMenuSwitch = (ImageView) switchLayout.getChildAt(1);
        mAccountsMenuSwitch.setImageResource(R.drawable.ic_arrow_drop_down);
        mAccountsMenuSwitch.setVisibility(GONE);
    }

    @Override
    protected void updateMoreAccountsList() {
        if (mAccounts != null && mAccounts.size() > 3 &&
                mAccountsMenuItems != null && mAccountsMenuAdapter != null) {
            NavigationDrawerAccountsListItemAccount moreAccount;
            for (int i = (mAccounts.size() - 1); i >= 3; --i) {
                Account account = mAccounts.get(i);
                moreAccount = new NavigationDrawerAccountsListItemAccount(getContext());
                moreAccount.setTitle(account.getTitle());
                if (account.usePictureUrl()) moreAccount.setIcon(account.getPictureUrl());
                else moreAccount.setIcon(account.getPictureResource());
                moreAccount.setOnClickListener(generateAccountClickListener(i));
                mAccountsMenuItems.add(0, moreAccount);
            }
            mAccountsMenuAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected OnMoreAccountClickListener generateAccountClickListener(final int accountId) {
        return new OnMoreAccountClickListener() {

            @Override
            public void onMoreAccountClick(View v, int i) {

                int tmpPosition = mAccountsPositions[0];
                mAccountsPositions[0] = accountId;

                Account account = mAccounts.get(mAccountsPositions[1]);
                NavigationDrawerAccountsListItemAccount moreAccount =
                        new NavigationDrawerAccountsListItemAccount(getContext());
                moreAccount.setTitle(account.getTitle());
                if (account.usePictureUrl()) moreAccount.setIcon(account.getPictureUrl());
                else moreAccount.setIcon(account.getPictureResource());
                moreAccount.setOnClickListener(
                        generateAccountClickListener(mAccountsPositions[1]));
                mAccountsMenuItems.remove(i - 1);
                mAccountsMenuItems.add(0, moreAccount);
                mAccountsMenuAdapter.notifyDataSetChanged();

                mAccountsPositions[1] = mAccountsPositions[2];
                mAccountsPositions[2] = tmpPosition;
                changeAccount();
                if (mOnAccountChangeListener != null) {
                    mOnAccountChangeListener
                            .onAccountChange(mAccounts.get(mAccountsPositions[0]));
                }
            }

        };
    }


}
