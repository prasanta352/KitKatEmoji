package com.sithagi.kitkatemoji;

import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.content.Intent;
import ohos.accessibility.AccessibilityEventInfo;
import ohos.accessibility.ability.AccessibleAbility;
import ohos.accessibility.ability.SoftKeyBoardController;
import ohos.accessibility.ability.SoftKeyBoardListener;
import ohos.agp.components.*;
import ohos.agp.components.TextField;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.Rect;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.inputmethodability.InputMethodAbility;

import static ohos.agp.window.service.WindowManager.LayoutConfig.INPUT_ADJUST_RESIZE;


/**
 * MainAbility.
 */
public class MainAbility extends AccessibleAbility {
    TextField messageEd;
    DirectionalLayout emojiIconsCover;
    EmojiconsFraction emojiconsFraction;
    DirectionalLayout parentLayout;
    TextField messageTx;
    Image btnChatEmoji;
    boolean isEmojiKeyboardVisible = false;
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");
    Text output;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        super.setUIContent(ResourceTable.Layout_ability_main);
        getWindow().setInputPanelDisplayType(INPUT_ADJUST_RESIZE);
        messageTx = (TextField) findComponentById(ResourceTable.Id_txt_sentMessage);
        parentLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_chatfragment_parent);
        output = (Text) findComponentById(ResourceTable.Id_output);
        findComponentById(ResourceTable.Id_openKeyboard).setClickedListener(c -> {
            showKeyBoard();
        });

        findComponentById(ResourceTable.Id_closeKeyboard).setClickedListener(c -> {
            hideKeyBoard();
        });


        getSoftKeyBoardController().addListener((softKeyBoardController, i) -> {
            HiLog.warn(LABEL_LOG, "MainAbility: onSoftKeyBoardShowModeChanged " + i);
            new ToastDialog(getContext()).setText("MainAbility: onSoftKeyBoardShowModeChanged " + i).setAlignment(LayoutAlignment.TOP).show();
        });

        try {
            checkKeyboardHeight(parentLayout);

        } catch (Exception exception) {
            new ToastDialog(getContext()).setText("onGlobalLayoutUpdated " + exception).setAlignment(LayoutAlignment.BOTTOM).show();

            HiLog.warn(LABEL_LOG, "" + exception);
            for (StackTraceElement st : exception.getStackTrace()) {
                HiLog.warn(LABEL_LOG, "" + st);
            }
        }
    }

    int previousHeightDiffrence = 0;

    private void checkKeyboardHeight(final Component parentLayout) {
        parentLayout.getComponentTreeObserver().addTreeLayoutChangedListener(new ComponentTreeObserver.GlobalLayoutListener() {
            @Override
            public void onGlobalLayoutUpdated() {
                new ToastDialog(getContext()).setText("onGlobalLayoutUpdated").setAlignment(LayoutAlignment.BOTTOM).show();
                Rect r = new Rect();
                parentLayout.getWindowVisibleRect(r);
                int screenHeight = parentLayout.getHeight();
                int heightDifference = screenHeight - (r.bottom);
                output.setText("" + r +
                        "\nscreenHeight= " + screenHeight +
                        "\nheightDifference: heightDifference" + heightDifference +
                        "\npreviousHeightDiffrence: " + previousHeightDiffrence
                );
                previousHeightDiffrence = heightDifference;
            }
        });
    }

    private void showKeyBoard() {
        messageTx.requestFocus();//set focus in text field
        messageTx.simulateClick(); // brings keyboard up
        new ToastDialog(getContext()).setText("showKeyBoard").setAlignment(LayoutAlignment.BOTTOM).show();
    }

    private void hideKeyBoard() {
        messageTx.clearFocus();
        new ToastDialog(getContext()).setText("hideKeyBoard").setAlignment(LayoutAlignment.BOTTOM).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEventInfo accessibilityEventInfo) {

    }

    @Override
    public void onInterrupt() {

    }
}
