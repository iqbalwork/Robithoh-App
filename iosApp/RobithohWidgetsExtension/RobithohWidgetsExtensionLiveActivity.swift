//
//  RobithohWidgetsExtensionLiveActivity.swift
//  RobithohWidgetsExtension
//
//  Created by Iqbal Bobobox on 07/09/26.
//

import ActivityKit
import WidgetKit
import SwiftUI

struct RobithohWidgetsExtensionAttributes: ActivityAttributes {
    public struct ContentState: Codable, Hashable {
        // Dynamic stateful properties about your activity go here!
        var emoji: String
    }

    // Fixed non-changing properties about your activity go here!
    var name: String
}

struct RobithohWidgetsExtensionLiveActivity: Widget {
    var body: some WidgetConfiguration {
        ActivityConfiguration(for: RobithohWidgetsExtensionAttributes.self) { context in
            // Lock screen/banner UI goes here
            VStack {
                Text("Hello \(context.state.emoji)")
            }
            .activityBackgroundTint(Color.cyan)
            .activitySystemActionForegroundColor(Color.black)

        } dynamicIsland: { context in
            DynamicIsland {
                // Expanded UI goes here.  Compose the expanded UI through
                // various regions, like leading/trailing/center/bottom
                DynamicIslandExpandedRegion(.leading) {
                    Text("Leading")
                }
                DynamicIslandExpandedRegion(.trailing) {
                    Text("Trailing")
                }
                DynamicIslandExpandedRegion(.bottom) {
                    Text("Bottom \(context.state.emoji)")
                    // more content
                }
            } compactLeading: {
                Text("L")
            } compactTrailing: {
                Text("T \(context.state.emoji)")
            } minimal: {
                Text(context.state.emoji)
            }
            .widgetURL(URL(string: "http://www.apple.com"))
            .keylineTint(Color.red)
        }
    }
}

extension RobithohWidgetsExtensionAttributes {
    fileprivate static var preview: RobithohWidgetsExtensionAttributes {
        RobithohWidgetsExtensionAttributes(name: "World")
    }
}

extension RobithohWidgetsExtensionAttributes.ContentState {
    fileprivate static var smiley: RobithohWidgetsExtensionAttributes.ContentState {
        RobithohWidgetsExtensionAttributes.ContentState(emoji: "😀")
     }
     
     fileprivate static var starEyes: RobithohWidgetsExtensionAttributes.ContentState {
         RobithohWidgetsExtensionAttributes.ContentState(emoji: "🤩")
     }
}

#Preview("Notification", as: .content, using: RobithohWidgetsExtensionAttributes.preview) {
   RobithohWidgetsExtensionLiveActivity()
} contentStates: {
    RobithohWidgetsExtensionAttributes.ContentState.smiley
    RobithohWidgetsExtensionAttributes.ContentState.starEyes
}
