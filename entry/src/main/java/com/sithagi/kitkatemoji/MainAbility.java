package com.sithagi.kitkatemoji;

import static ohos.agp.window.service.WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.TextField;
import ohos.agp.utils.Rect;


/**
 * MainAbility.
 */
public class MainAbility extends FractionAbility {
    Image btnChatEmoji;
    TextField messageTx;
    TextField messageEd;
    DirectionalLayout parentLayout;
    DirectionalLayout emojiIconsCover;
    EmojiconsFraction emojiconsFraction;
    int screenHeight = 0;
    int keyboardHeight = 0;
    int previousHeightDifference = 0;
    boolean isEmojiKeyboardVisible = false;
    boolean isDeviceKeyBoardVisible = false;



    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        super.setUIContent(ResourceTable.Layout_ability_main);
        getWindow().setInputPanelDisplayType(INPUT_ADJUST_RESIZE);


        emojiconsFraction = new EmojiconsFraction(getContext());

        messageEd = (TextField) findComponentById(ResourceTable.Id_edit_chat_message);
        messageTx = (TextField) findComponentById(ResourceTable.Id_txt_sentMessage);
        emojiIconsCover = (DirectionalLayout) findComponentById(ResourceTable.Id_emoji_keyboard_fraction);
        btnChatEmoji = (Image) findComponentById(ResourceTable.Id_btn_chat_emoji);
        parentLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_parentLayout);
        Image sendButton = (Image) findComponentById(ResourceTable.Id_btn_send);

        messageEd.setTouchEventListener((component, m) -> {
            if (!isDeviceKeyBoardVisible && !isEmojiKeyboardVisible) {
                showKeyBoard();
                btnChatEmoji
                        .setPixelMap(ResourceTable.Media_ic_vp_smileys);
                emojiIconsCover
                        .setVisibility(Component.HIDE);
                isEmojiKeyboardVisible = false;
            }
            return false;
        });


        sendButton.setClickedListener(c -> {
            String chat = messageEd.getText().trim();
            if (!chat.isEmpty()) {
                messageTx.setText(chat);
                messageEd.setText("");
            }
            if (isEmojiKeyboardVisible) {
                hideEmojiKeyboard();
            }

        });

        btnChatEmoji.setClickedListener(c -> changeEmojiLayout());

        emojiconsFraction.setOnEmojiIconClickedListener(emojicon -> emojiconsFraction.input(messageEd, emojicon));

        emojiconsFraction.setOnEmojiIconBackspaceClickedListener(c -> emojiconsFraction.backspace(messageEd));

        getFractionManager()
                .startFractionScheduler()
                .add(ResourceTable.Id_emoji_keyboard_fraction, emojiconsFraction)
                .submit();
        checkKeyboardHeight(parentLayout);

    }



    private void hideEmojiKeyboard() {
        btnChatEmoji
                .setPixelMap(ResourceTable.Media_ic_vp_smileys);
        emojiIconsCover
                .setVisibility(Component.HIDE);
        isEmojiKeyboardVisible = false;
    }

    private void showEmojiKeyboard() {
        btnChatEmoji
                .setPixelMap(ResourceTable.Media_ic_vp_keypad);
        emojiIconsCover
                .setVisibility(Component.VISIBLE);
        isEmojiKeyboardVisible = true;
    }

    protected void changeEmojiLayout() {
        if (isEmojiKeyboardVisible) {
            if (!isDeviceKeyBoardVisible) {
                hideEmojiKeyboard();
                showKeyBoard();
            }
        } else {
            if (isDeviceKeyBoardVisible) {
                hideKeyBoard();
                getUITaskDispatcher().delayDispatch(this::showEmojiKeyboard, 200);
            } else {
                hideKeyBoard();
                showEmojiKeyboard();
            }
        }
    }

    void showKeyBoard() {
        messageEd.requestFocus();
        messageTx.simulateClick();
    }

    void hideKeyBoard() {
        messageEd.clearFocus();
    }

    private void checkKeyboardHeight(final Component parentLayout) {
        parentLayout.setLayoutRefreshedListener(c -> {
            Rect r = new Rect();
            parentLayout.getWindowVisibleRect(r);

            if (screenHeight < r.getHeight()) {
                screenHeight = r.getHeight();
            }

            int heightDifference = screenHeight - (r.bottom);

            if (screenHeight == heightDifference) {
                return;
            }

            if (previousHeightDifference - heightDifference > 50) {
                btnChatEmoji.setPixelMap(ResourceTable.Media_ic_vp_smileys);
                emojiIconsCover.setVisibility(Component.HIDE);
            }
            previousHeightDifference = heightDifference;

            if (heightDifference > 100) {
                isDeviceKeyBoardVisible = true;
                changeKeyboardHeight(heightDifference);
            } else {
                isDeviceKeyBoardVisible = false;
            }
        });
    }


    /**
     * change height of emoticons keyboard according to height of actual
     * keyboard.
     *
     * @param height minimum height by which we can make sure actual keyboard is
     *               open or not
     */
    private void changeKeyboardHeight(int height) {
        if (height > 100) {
            keyboardHeight = height;
            emojiIconsCover.setHeight(height);
        }
    }
}
