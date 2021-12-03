package com.sithagi.kitkatemoji;

import static ohos.agp.window.service.WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE;

import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.TextField;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.Rect;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;


/**
 * MainAbility.
 */
public class MainAbility extends FractionAbility {
    TextField messageEd;
    DirectionalLayout emojiIconsCover;
    EmojiconsFraction emojiconsFraction;
    TextField messageTx;
    Image btnChatEmoji;
    DirectionalLayout parentLayout;
    boolean isEmojiKeyboardVisible = false;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        super.setUIContent(ResourceTable.Layout_ability_main);
        getWindow().setInputPanelDisplayType(INPUT_ADJUST_RESIZE);


        emojiconsFraction = new EmojiconsFraction(getContext());

        messageEd = (TextField) findComponentById(ResourceTable.Id_edit_chat_message);
        messageTx = (TextField) findComponentById(ResourceTable.Id_txt_sentMessage);
        emojiIconsCover = (DirectionalLayout) findComponentById(ResourceTable.Id_main_fraction);
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

        getFractionManager().startFractionScheduler().add(ResourceTable.Id_main_fraction, emojiconsFraction)
                .submit();
        HiLog.warn(LABEL_LOG, "MainAbility: onStart");

        checkKeyboardHeight(parentLayout);

    }

    boolean isDeviceKeyBoardVisible = false;


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
        HiLog.warn(LABEL_LOG, "MainAbility: isEmojiKeyboardVisible " + isEmojiKeyboardVisible);
        HiLog.warn(LABEL_LOG, "MainAbility: isDeviceKeyBoardVisible " + isDeviceKeyBoardVisible);
        if (isEmojiKeyboardVisible) {
            if (!isDeviceKeyBoardVisible) {
                hideEmojiKeyboard();
                showKeyBoard();
                HiLog.warn(LABEL_LOG, "changeEmojiLayout: 111");
            } else {
                HiLog.warn(LABEL_LOG, "MainAbility: changeEmojiLayout LOL");
            }
        } else {
            if (isDeviceKeyBoardVisible) {
                hideKeyBoard();
                getUITaskDispatcher().delayDispatch(() -> {

                    showEmojiKeyboard();
                    HiLog.warn(LABEL_LOG, "changeEmojiLayout: 222");
                }, 200);
            } else {
                hideKeyBoard();
                showEmojiKeyboard();
                HiLog.warn(LABEL_LOG, "changeEmojiLayout: 333");
            }
        }

//        if (isEmojiKeyboardVisible && !isDeviceKeyBoardVisible) {
//
//        } else if (!isEmojiKeyboardVisible && isDeviceKeyBoardVisible) {
//
//        } else if (!isEmojiKeyboardVisible && !isDeviceKeyBoardVisible) {
//
//            btnChatEmoji
//                    .setPixelMap(ResourceTable.Media_ic_vp_keypad);
//            emojiIconsCover
//                    .setVisibility(Component.VISIBLE);
//            isEmojiKeyboardVisible = true;
//            HiLog.warn(LABEL_LOG, "changeEmojiLayout: 333");
//        }
    }

    void showKeyBoard() {
        HiLog.warn(LABEL_LOG, "MainAbility: showKeyBoard");
        messageEd.requestFocus();
        messageTx.simulateClick();
    }

    void hideKeyBoard() {
        HiLog.warn(LABEL_LOG, "MainAbility: hideKeyBoard");
        messageEd.clearFocus();
    }

    int previousHeightDiffrence = 0;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");
    int screenHeight = 0;


    private void checkKeyboardHeight(final Component parentLayout) {
        parentLayout.setLayoutRefreshedListener(c -> {
            try {
                Rect r = new Rect();
                parentLayout.getWindowVisibleRect(r);

                if (screenHeight < r.getHeight()) {
                    screenHeight = r.getHeight();
                }


                int heightDifference = screenHeight - (r.bottom);

                if (screenHeight == heightDifference) {
                    return;
                }

                if (previousHeightDiffrence - heightDifference > 50) {
                    btnChatEmoji.setPixelMap(ResourceTable.Media_ic_vp_smileys);
                    emojiIconsCover.setVisibility(Component.HIDE);
                }
                previousHeightDiffrence = heightDifference;

                if (heightDifference > 100) {
                    isDeviceKeyBoardVisible = true;
                    changeKeyboardHeight(heightDifference);
                } else {
                    isDeviceKeyBoardVisible = false;

                }
            } catch (Exception exception) {
                new ToastDialog(getContext()).setText("onGlobalLayoutUpdated " + exception).setAlignment(LayoutAlignment.BOTTOM).show();
                HiLog.warn(LABEL_LOG, "" + exception);
                for (StackTraceElement st : exception.getStackTrace()) {
                    HiLog.warn(LABEL_LOG, "" + st);
                }
            }
        });
    }

    int keyboardHeight;

    /**
     * change height of emoticons keyboard according to height of actual
     * keyboard
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
