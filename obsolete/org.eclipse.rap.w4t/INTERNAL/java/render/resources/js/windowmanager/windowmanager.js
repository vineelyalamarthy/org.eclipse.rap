/* This is the window manager libary with the object with
 * window handling functionality for the w4Toolkit,
 * Copyright 2001 by InnooPract */

  // the window manager prototype
  function WindowManager() {

    // form variables for the WebForm control
    this.posX = 0;
    this.posY = 0;
  
    this.requestCounter   = 0;

    this.initMethods = new Array( 0 );

    // methods of the window manager object

    // initialises the current page of the active WebForm
    this.initWebForm = initWebForm;

    // adds a method to be called in the initialization process
    this.addInitMethod = wm_addInitMethod;

    // sets the name of the browser instance according to 
    // the actually displayed WebForm,
    // param wfID the ID of the WebForm which is displayed 
    //            to open in the window
    this.setName = setName;    

    // if the WebForm displayed on this page creates a new
    // browser window
    this.openNewWindow = openNewWindow;

    // if the WebForm displayed on this page reloads 
    // WebForms in other browser windows
    this.refreshWindow = refreshWindow;
  
    // closes this window at onLoad
    this.closeWindow = closeWindow;

    // submits the current WebForm to refresh its timestamp
    this.triggerTimeStamp = triggerTimeStamp;

    // sets the window content to the specified scrollbar position
    this.scrollWindow = scrollWindow;
    
    // prints the page
    this.printPage = printPage;
  }

  /**********************************************************/ 
  /* the section with the standard window manager functions */
  /**********************************************************/ 

  // initialises the current page of the active WebForm
  function initWebForm( enableBrowserNavigation ) {
    if( enableBrowserNavigation != 'true' ) {
      history.forward();  	      	
    }
    this.requestCounter = document.getElementById( "requestCounter" ).value;
    if( this.requestCounter == 0 ) {
      if( this.posX != -1 && this.posY != -1 ) {
        try {
          window.moveTo( this.posX, this.posY );
        } catch( e ) {
          // ignore, IE might throw when dragging the window while moveTo() 
          // is called
        }
      }
    }
    for( var i = 0; i < this.initMethods.length; i++ ) {
      setTimeout( '' + this.initMethods[ i ] + '', 0 );
    }
  } 
  
  // adds a method to be called in the initialization process
  function wm_addInitMethod( methodCall ) {
    var size = 0;
    if( this.initMethods != null ) {
      size = this.initMethods.length; 	
    }
    buffer = new Array( size + 1 );
    for( var i = 0; i < size; i++ ) {
      buffer[ i ] = this.initMethods[ i ];
    }
    buffer[ size + 1 ] = methodCall;
    this.initMethods = buffer;    
  }

  // sets the name of the browser instance according to 
  // the actually displayed WebForm,
  // param wfID the ID of the WebForm which is displayed 
  //            to open in the window
  function setName( wfID ) {
    window.name = wfID;
  }


  // if the WebForm displayed on this page creates a new
  // browser window
  function openNewWindow( url, wfID, props ) {
    eventHandler.unload = false;
    var windowRef = open( url, wfID, props );
    if( windowRef != null ) {
      windowRef.focus();
    }
  } 


  // if the WebForm displayed on this page reloads 
  // WebForms in other browser windows
  function refreshWindow( url, wfID, props ) {
    open( url, wfID, props ); 
  } 
  

  // closes this window at onLoad
  function closeWindow() {	
    window.close();
  }

  // submits the current WebForm to refresh its timestamp
  function triggerTimeStamp() {
    var action = document.W4TForm.action;
    var indicator = document.W4TForm.w4tEngineIndicator.value;
    var uiRoot = document.W4TForm.uiRoot.value;
    w1 = open(   action 
               + "&requestCounter=" + ( this.requestCounter - 1 )
               + "&uiRoot=" + this.uiRoot,
               "triggerTimeStamp" + this.uiRoot, 
               "width=1,height=1,screenX=0,screenY=0" );              
    setTimeout( 'w1.close();', 50 );
  }  

  // sets the window content to the specified scrollbar position
  function scrollWindow( scrollX, scrollY ) {
    window.scrollTo( scrollX, scrollY );
  }
  
  // prints the page 
  function printPage() {
    window.print();	
  }


  /**********************************************************/ 
  /*          create the window manager instance            */
  /**********************************************************/ 

  var windowManager = new WindowManager();

  
  /**********************************************************/ 
  /*          declaration of global vars etc.               */
  /**********************************************************/ 

  var active = 0;

  function w4tClearKeepAlive() {
    if( active && active != 0 ) {
      window.clearInterval( active ); 
    }
  }

  /**********************************************************/ 
  /*   New added function to refresh its current WebForm    */
  /*   timestamp, based on the manager instance.            */
  /**********************************************************/ 

  function triggerTimeStamp_DOM() {
    var action = document.W4TForm.action;
    var indicator = document.getElementById( "w4tEngineIndicator" ).value;
    var triggerTimeStampImg 
      = document.getElementById( "w4tTriggerTimeStampImg" );
    var uiRoot = document.getElementById( "uiRoot" ).value;
    // the random-parameter forces the browser to not take the image from cache
    var url =  action 
             + "&requestCounter=" + ( windowManager.requestCounter - 1 )
             + "&uiRoot=" + uiRoot
             + "&w4tTriggerTimeStamp=true"
             + "&w4tRandom=" + String( Math.random() ); 
    triggerTimeStampImg.src = url;
  }
