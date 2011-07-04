Console Beep
-------------

Console Beep is an IntelliJ IDEA/RubyMine/PhpStorm/WebStorm/PyCharm plugin that plays a sound when a certain text appears in console.

The motivation is to give a developer some spare time while his application is starting, but give distinct notification that the spare time has ended and it's time to work.

My typical scenario is:

1. Write some code that can not be hotswapped in running VM (provided we do not use JRebel-like tools)
2. Kill the app and start the app
3. Wait until it starts
4. Test new code

Now, the problem is 3. - you must wait. Say, 20-30 seconds - pretty typical for my Jetty + Spring + Hibernate powered projects. You can spend this time in a few ways. First - you can hypnotize your console, waiting for the app to start, so that you don't miss the moment and do not distract yourself with reading mail, your Google Reader subscriptions and stuff. Second - you can Alt+Tab your IDE and go read your mail, Google Reader and stuff. Second option is dangerous - you can forget that you have started something you must wait for. This is a productivity killer, indeed. Third option is to go edit some more code, but what code can you think of when you got half a minute only?

My solution is this plugin. I wrote it for myself in the first place, it does help me. So, what you do is select a last message your app says when it is ready to work (this is something like `Slf4jLog: Started SelectChannelConnector@0.0.0.0:3333` for Jetty), right-click for context menu and select "Beep on That". Now, every time I restart the app I can select the second option (go procrastinate for half a minute), but now there is a distinct indicator that I must stop procrastinating and the app has started - a ding sound. Now a "ding" in my head means GET BACK TO WORK, this is not a background thoughts like "let's check if it has started and then get back to work", but a concrete message - "the app has started, go test it!". And my productivity has increased up to 42% - amazing results!

My plan is to provide double and triple dings, so that one could mark certain landmarks in starting process, like 1 ding - ready (applicationContext initialized), 2 dings - set (Hibernate SessionFactory initialized), 3 dings - go (all handler mappings initialized and socket is listening)! Maybe some more sounds, not dings, but also dongs and booms.