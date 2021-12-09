KitKatEmoji
===========

Library for Emoji View like Hangouts, Emoji TextView and Emoji EditText

<img src="assets/preview.gif" height="500" /> 


### Source
---
This library has been inspired by [chathudan/KitKatEmoji](https://github.com/chathudan/KitKatEmoji)

### Integration
---

**From Source**
1. For using KitKatEmoji module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.
    ```groovy
    implementation project(path: ':kitkatemoji')
    ```
2. For using KitKatEmoji module in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.
    ```groovy
   implementation fileTree(dir: 'libs', include: ['*.har'])
   ```
## Usage
 

```xml
<!-- create a container to put the emojikeyboard -->
 <DirectionalLayout
        ohos:id="$+id:emoji_keyboard_fraction"
        ohos:height="250vp"
        ohos:width="match_parent"
        ohos:visibility="hide"
        ohos:background_element="#FFFFEEAD"
        />

```

```java
// extends the FractionAbility
public class MainAbility extends FractionAbility {  
    @Override
    void onStart(){
        // create the emoji keyboard
        EmojiconsFraction emojiconsFraction = new EmojiconsFraction(getContext());
        // add listeners
        emojiconsFraction.setOnEmojiIconClickedListener(emojicon -> emojiconsFraction.input(messageEd, emojicon));
        
        emojiconsFraction.setOnEmojiIconBackspaceClickedListener(c -> emojiconsFraction.backspace(messageEd));
    
        // add the emoji icon to the container
        getFractionManager()
                        .startFractionScheduler()
                        .add(ResourceTable.Id_emoji_keyboard_fraction, emojiconsFraction)
                        .submit();
    }
}
```

for complete example please take a look at the [sample](entry) application.
## License

KitKatEmoji is released under the [Apache License Version 2.0](LICENSE.md).
