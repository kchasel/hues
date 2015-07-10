package hues.lighting

import com.philips.lighting.hue.listener._
import com.philips.lighting.hue.sdk._
import com.philips.lighting.model._

import scala.collection.JavaConversions._

object Lighting {
  val phHueSDK = PHHueSDK.getInstance
  var bridges: List[PHAccessPoint] = List()
  var bridge: Option[PHBridge] = None

  phHueSDK.setAppName("hues")
  phHueSDK.setDeviceName("macosx")

  private val listener = new PHSDKListener() {

    def onAccessPointsFound(accessPoint: java.util.List[PHAccessPoint]) = {
      println("FOUND " + accessPoint + "; connecting")
      bridges = accessPoint.toList
    }
    
    @Override
    def onCacheUpdated(cacheNotificationsList: java.util.List[Integer], bridge: PHBridge) = {
         // Here you receive notifications that the BridgeResource Cache was updated. Use the PHMessageType to   
         // check which cache was updated, e.g.
        if (cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) {
           //System.out.println("Lights Cache Updated ");
        }
    }

    @Override
    def onBridgeConnected(b: PHBridge) = {
        println("BRIDGE CONNECTED: " + b)
        bridge = Some(b)
        phHueSDK.setSelectedBridge(b);
        phHueSDK.enableHeartbeat(b, PHHueSDK.HB_INTERVAL);
        // Here it is recommended to set your connected bridge in your sdk object (as above) and start the heartbeat.
        // At this point you are connected to a bridge so you should pass control to your main program/activity.
        // Also it is recommended you store the connected IP Address/ Username in your app here.  This will allow easy automatic connection on subsequent use. 
    }

    @Override
    def onAuthenticationRequired(accessPoint: PHAccessPoint) = {
        println("NEED TO PRESS BUTTON")
        phHueSDK.startPushlinkAuthentication(accessPoint);

        // Arriving here indicates that Pushlinking is required (to prove the User has physical access to the bridge).  Typically here
        // you will display a pushlink image (with a timer) indicating to to the user they need to push the button on their bridge within 30 seconds.
    }

    @Override
    def onConnectionResumed(bridge: PHBridge) = {

    }

    @Override
    def onConnectionLost(accessPoint: PHAccessPoint) = {
         // Here you would handle the loss of connection to your bridge.
    }

    @Override
    def onError(code: Int, message: String) = {
         println("ERROR " + code + " " + message)
         // Here you can handle events such as Bridge Not Responding, Authentication Failed and Bridge Not Found
    }

    @Override
    def onParsingErrors(parsingErrorsList: java.util.List[PHHueParsingError]) = {
        // Any JSON parsing errors are returned here.  Typically your program should never return these.      
    }
  }

  phHueSDK.getNotificationManager().registerSDKListener(listener)
  val searchMgr: PHBridgeSearchManager = phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE).asInstanceOf[PHBridgeSearchManager]

  def connect = searchMgr.search(true, true)
}
