ZoomR8Control
=============

A class to simplify the usage of Zoom's R8 controller in SuperCollider.
This work is based on the [NanoKontrol.sc](http://github.com/jesusgollonet/NanoKontrol.sc) class implemented by jesusgollonet. 

Install instructions
=============
just place a copy of the ZoomR8Control.sc in your SuperCollider extensions folder:

**User-specific**

*OSX :* ~/Library/Application Support/SuperCollider/Extensions/

*Linux :* ~/share/SuperCollider/Extensions/

```
Platform.userExtensionDir
```

**System-wide (apply to all users)**

*OSX :* /Library/Application Support/SuperCollider/Extensions/

*Linux :* /usr/local/share/SuperCollider/Extensions/

```
Platform.systemExtensionDir
```

Usage instructions
=============

create an instance
```
    z = ZoomR8Control.new
```
assign actions
```
    z.fader1.onChanged = {|val| 
        "fader 1 changed".postln;
        val.postln;
    };

    z.faderBt1.onPress   = {|val| "fader button 1 pressed".postln;};
                          
    z.faderBt1.onRelease = {|val| "fader button 1 released".postln;};
```
actions will be overwritten when assigned to the same item
```
    z.faderBt1.onPress  = {|val| "fader button 1 used".postln; val.postln;};
```
you can assign multiple items directly

faders 1-9
```
    z.faders.do{|fader, i| 
        fader.onChanged= {|val| ("fader"+(i+1)).postln; val.postln }
    };
```
assign fader buttons 1-4 only
```
    z.faderbuts[..3].do{|knob| knob.onChanged= {|val| val.postln } };
```
remove all MIDI responders
```
    z.removeAll;
```
Controller names
----------------
###the faders (send pitchbend values between 0 and 16383)###
* `fader1..9`

###the buttons above the faders (send note on & noteoff events, 0 on release and 127 on press)###
* `faderBt1..8` (the master button does not work)
* `fBut1..5`

###transport buttons###

* `rewBt`
* `ffBt`
* `stopBt`
* `playBt`
* `recBt`

###arrow buttons (next to the jog wheel)###

* `upArrow`
* `downArrow`
* `leftArrow`
* `rightArrow`

###bank buttons (below the display)###

* `bankLeft`
* `bankRight`

Actions 
-------
* `onChanged` can be applied to all controller names
* `onPress`, `onRelease` can only be applied to the fader buttons, the function buttons, the arrows and the transport buttons
* all actions receive the control value as an argument 
