# IntentBridge

This lets you activate arbitrary Android intents via the web browser!

## Problem

I have a movie file, and a subtitle file, and want to present them in a website
in such a way that I can use an Android device to view the movie file
*together* with the subtitle. Unfortunately (and rather obviously), this was
not possible in a convenient way. You can present a link to the movie file and
another link for the subtitle file, but there would be multiple steps required
to view them:

1. Download the subtitle file
2. Click on the link to the movie file (a movie player will pop up)
3. Tell the movie player to load the subtitle file downloaded in step (1)

## IntentBridge to the rescue!

IntentBridge is an Android app, that captures the URL that you click on,
performs magic on the URL and pass the movie file *plus* the subtitle file to
the movie player. The movie player will now play the movie file *together* with
the subtitle!.

Okay it's not really magic. You construct the URL in such a way that it
contains both URLs, and IntentBridge splits that URL.

## How does this *really* work?

Here is the structure of the magic URL:

http://phunehehe.github.io/IntentBridge /`action`/`target_uri`/`extras`

  - `action` is the intent action, such as `android.intent.action.VIEW`.
  - `target_uri` is the URI that is passed to the intent. In our movie example,
    it's the URL of the movie, URL-encoded.
  - `extras` is the list of "extras" to pass to the intent, in JSON format,
    URL-encoded.

In our example, `extras` in its full form is as follow (as per [MX Player's
API](https://sites.google.com/site/mxvpen/api)):

    [
        {
            "name": "subs",
            "type": "android.net.Uri[]",
            "value": [
                "http://phunehehe.github.io/IntentBridge/demo.srt"
            ]
        }
    ]

Install
[MX Player](https://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad),
[IntentBridge](http://d.pr/f/Fy80),
use your Android device and
[click here](http://phunehehe.github.io/IntentBridge/android.intent.action.VIEW/http%3A%2F%2Fphunehehe.github.io%2FIntentBridge%2Fsample_mpeg4.mp4/%5B%7B%22name%22%3A%22subs%22%2C%22type%22%3A%22android.net.Uri%5B%5D%22%2C%22value%22%3A%5B%22http%3A%2F%2Fphunehehe.github.io%2FIntentBridge%2Fdemo.srt%22%5D%7D%5D)
to convince yourself that it actually works.

## So this is a kind of movie player?

Not really, it can be use to trigger any Android intent, but the movie playing
part scratches my itch :)
