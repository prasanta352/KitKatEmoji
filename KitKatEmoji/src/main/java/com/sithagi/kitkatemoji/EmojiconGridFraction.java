package com.sithagi.kitkatemoji;

import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TableLayoutManager;
import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import com.sithagi.kitkatemoji.emoji.Emojicon;
import java.util.Optional;

/**
 * EmojiconGridFraction.
 */
public class EmojiconGridFraction extends Fraction implements EmojiIconProvider.OnEmojiIconClickedListener {

    private final Emojicon[] emojicons;
    private EmojiIconProvider.OnEmojiIconClickedListener onEmojiIconClickedListener;
    Component component;
    Context context;

    /**
     * Create a EmojiGridFraction instance.
     *
     * @param context                    application context
     * @param emojicons                  list of emojiIcons
     * @param onEmojiIconClickedListener onItemClick listener
     * @return instance of a EmojiconGridFraction
     */
    protected static EmojiconGridFraction newInstance(
            Context context,
            Emojicon[] emojicons,
            EmojiIconProvider.OnEmojiIconClickedListener onEmojiIconClickedListener
    ) {
        EmojiconGridFraction emojiGridFraction = new EmojiconGridFraction(context, emojicons);
        emojiGridFraction.setOnEmojiIconClickedListener(onEmojiIconClickedListener);
        return emojiGridFraction;
    }

    /**
     * get device screen width.
     *
     * @return screen width
     */
    private int getScreenWidth() {
        DisplayManager displayManager = DisplayManager.getInstance();
        Optional<Display> optDisplay = displayManager.getDefaultDisplay(context);
        Point point = new Point(0, 0);
        if (optDisplay.isPresent()) {
            Display display = optDisplay.get();
            display.getSize(point);
        }
        return (int) point.position[0];
    }

    /**
     * EmojiconGridFraction.
     *
     * @param context   application context
     * @param emojicons emojiIcon list
     */
    public EmojiconGridFraction(Context context, Emojicon[] emojicons) {
        this.context = context;
        this.emojicons = emojicons;
    }

    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_emojicon_grid, container, false);
        return component;
    }

    @Override
    public Component getComponent() {
        component = LayoutScatter.getInstance(context).parse(
                ResourceTable.Layout_emojicon_grid,
                null,
                false
        );

        ListContainer listContainer = (ListContainer) component.findComponentById(ResourceTable.Id_Emoji_GridView);
        final TableLayoutManager tableLayoutManager = new TableLayoutManager();
        final EmojiIconProvider emojiIconProvider = new EmojiIconProvider(emojicons, context, this);

        int screenWidth = getScreenWidth();
        int cols = screenWidth / AttrHelper.fp2px(34f, context);

        tableLayoutManager.setColumnCount(cols);
        tableLayoutManager.setRowCount(emojicons.length / cols);

        listContainer.setLayoutManager(tableLayoutManager);
        listContainer.setItemProvider(emojiIconProvider);

        return component;
    }


    public void setOnEmojiIconClickedListener(EmojiIconProvider.OnEmojiIconClickedListener onEmojiIconClickedListener) {
        this.onEmojiIconClickedListener = onEmojiIconClickedListener;
    }

    @Override
    public void onEmojiIconClicked(Emojicon emojicon) {
        if (onEmojiIconClickedListener != null) {
            onEmojiIconClickedListener.onEmojiIconClicked(emojicon);
        }
    }

    @Override
    protected void onComponentDetach() {
        onEmojiIconClickedListener = null;
        super.onComponentDetach();
    }
}
