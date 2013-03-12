/*
 * SuperCollider class for Zoom-R8 controller
 * created by: bgc
 * based on the NanoKontrol.sc by jesusgollonet (http://github.com/jesusgollonet/NanoKontrol.sc/)
 */

/*
 TODO:
 	- scrollwheel! (uses cc 60, 65 <- && 1 ->)
*/

ZoomR8Control {
	var fadergroup, faderbuttons, arrowbuttons, transportbuttons, functionbuttons,bankbuttons;
	var <controllers;

	*new{
		^super.new.initZoomR8;
	}

	initZoomR8{

		MIDIIn.connectAll;

		fadergroup = IdentityDictionary[
			\fader1 -> ZR8Bender(\fader1, 0),
			\fader2 -> ZR8Bender(\fader2, 1),
			\fader3 -> ZR8Bender(\fader3, 2),
			\fader4 -> ZR8Bender(\fader4, 3),
			\fader5 -> ZR8Bender(\fader5, 4),
			\fader6 -> ZR8Bender(\fader6, 5),
			\fader7 -> ZR8Bender(\fader7, 6),
			\fader8 -> ZR8Bender(\fader8, 7),
			\fader9 -> ZR8Bender(\fader9, 8)
		];

		faderbuttons = IdentityDictionary[
			\faderBt1 -> ZR8Button(\faderBt1, 8),
			\faderBt2 -> ZR8Button(\faderBt2, 9),
			\faderBt3 -> ZR8Button(\faderBt3, 10),
			\faderBt4 -> ZR8Button(\faderBt4, 11),
			\faderBt5 -> ZR8Button(\faderBt5, 12),
			\faderBt6 -> ZR8Button(\faderBt6, 13),
			\faderBt7 -> ZR8Button(\faderBt7, 14),
			\faderBt8 -> ZR8Button(\faderBt8, 15),
		];

		transportbuttons = IdentityDictionary[
			\rewBt	-> ZR8Button(\rewBt, 91),
			\ffBt	-> ZR8Button(\ffBt, 92),
			\stopBt	-> ZR8Button(\stopBt, 93),
			\playBt	-> ZR8Button(\playBt, 94),
			\recBt	-> ZR8Button(\recBt, 95)
		];

		functionbuttons = IdentityDictionary[
			\fBut1 -> ZR8Button(\fBut1, 54),
			\fBut2 -> ZR8Button(\fBut2, 55),
			\fBut3 -> ZR8Button(\fBut3, 56),
			\fBut4 -> ZR8Button(\fBut4, 57),
			\fBut5 -> ZR8Button(\5But5, 58),
		];

		arrowbuttons = IdentityDictionary[
			\upArrow	-> ZR8Button(\upArrow, 96),
			\downArrow	-> ZR8Button(\downArrow, 97),
			\leftArrow	-> ZR8Button(\leftArrow, 98),
			\rightArrow	-> ZR8Button(\rightArrow, 99)
		];

		bankbuttons = IdentityDictionary[
			\bankLeft	-> ZR8Button(\bankLeft, 46),
			\bankRight	-> ZR8Button(\bankRight, 47)
		];

		controllers = IdentityDictionary.new;

		controllers.putAll(fadergroup, faderbuttons, transportbuttons, functionbuttons, arrowbuttons, bankbuttons);
	}

	faders{
		var align = fadergroup;
		align.order;
		^align.atAll(align.order);
	}

	faderbuts{
		var align = faderbuttons;
		align.order;
		^align.atAll(align.order);
	}

	transportbuts{
		var align = transportbuttons;
		align.order;
		^align.atAll(align.order);
	}

	fbuttons{
		var align = functionbuttons;
		align.order;
		^align.atAll(align.order);
	}

	arrows{
		var align = arrowbuttons;
		align.order;
		^align.atAll(align.order);
	}

	bankbuts{
		var align = arrowbuttons;
		align.order;
		^align.atAll(align.order);
	}

	removeAll{
		controllers.do{|cDict|
			cDict.do{|c| c.free}
		}
	}

	doesNotUnderstand {|selector ... args|
		var controller = controllers[selector];
		^controller ?? {
			super.doesNotUnderstand(selector, args)
		};
	}

}

ZR8Bender {

	var <key, <chan;
	var responder;

	*new{|... args|
		^super.newCopyArgs(*args);
	}

	onChanged_{|action|
		responder = MIDIdef.bend(key, {|val| action.value(val) }, chan);
	}

	free{
		responder.free;
	}
}


ZR8Controller {

	var <key, <num;
	var responder;

	*new{|... args|
		^super.newCopyArgs(*args);
	}

	onChanged_{|action|
		responder= MIDIdef.cc(key, {|val| action.value(val) }, num);
	}

	free{
		responder.free;
	}
}

ZR8ControllerNote {

	var <key, <num;
	var responder;

	*new{|... args|
		^super.newCopyArgs(*args);
	}

	onChanged_{|action|
		responder= MIDIdef.noteOn(key, {|val| action.value(val) }, num);
	}

	free{
		responder.free;
	}
}

//the R8 buttons send note-on/off events
ZR8Button : ZR8ControllerNote {

	var pressresp, releaseresp;

	onPress_{|action|
		pressresp= MIDIdef.noteOn((key++"press").asSymbol, {|val| if(val==127, { action.value(val) }) }, num);
	}

	onRelease_{|action|
		releaseresp= MIDIdef.noteOff((key++"release").asSymbol, {|val| if(val==0, { action.value(val) }) }, num);
	}

	free{
		pressresp.free;
		releaseresp.free;
	}
}