package com.sithagi.kitkatemoji;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;
import com.sithagi.kitkatemoji.emoji.Emojicon;

/**
 * EmojiIconProvider to render a single emoji icon.
 */
public class EmojiIconProvider extends BaseItemProvider {
    private final Emojicon[] list;
    private final Context slice;
    private final OnEmojiIconClickedListener onEmojiIconClickedListener;

    /**
     * constructor for creating a EmojiIconProvider.
     *
     * @param list list of the emoji icons
     * @param context application context
     * @param onEmojiIconClickedListener click listener for when an emoji icon is clicked
     */
    public EmojiIconProvider(Emojicon[] list, Context context, OnEmojiIconClickedListener onEmojiIconClickedListener) {
        this.list = list;
        this.slice = context;
        this.onEmojiIconClickedListener = onEmojiIconClickedListener;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.length;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position >= 0 && position < list.length) {
            return list[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        final Component cpt;
        if (component == null) {
            DirectionalLayout directionalLayout = new DirectionalLayout(slice);

            Component newCpt = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_emojicon_item, null, false);
            EmojiconTextView image = (EmojiconTextView) newCpt.findComponentById(ResourceTable.Id_emoji_icon);
            Emojicon emojicon = list[i];

            image.setText(emojicon.getEmoji());

            directionalLayout.addComponent(newCpt);
            cpt = directionalLayout;
            directionalLayout.setClickedListener(c -> onEmojiIconClickedListener.onEmojiIconClicked(emojicon));

        } else {
            cpt = component;
        }
        return cpt;
    }

    /**
     * Click listener for the EmojiIconProvider.
     */
    public interface OnEmojiIconClickedListener {
        void onEmojiIconClicked(Emojicon emojicon);
    }
}
