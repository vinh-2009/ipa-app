import SwiftUI
import UIKit

struct ContentView: View {
    @State private var selectedTab = 0
    
    // Core Colors
    let bgColor = Color(red: 0.05, green: 0.05, blue: 0.08)
    let accentColor = Color(red: 1.0, green: 0.3, blue: 0.0)
    
    init() {
        let appearance = UITabBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = UIColor(red: 0.05, green: 0.05, blue: 0.08, alpha: 1.0)
        
        let itemAppearance = UITabBarItemAppearance()
        itemAppearance.normal.iconColor = UIColor.systemGray
        itemAppearance.normal.titleTextAttributes = [.foregroundColor: UIColor.systemGray]
        
        itemAppearance.selected.iconColor = UIColor(red: 1.0, green: 0.3, blue: 0.0, alpha: 1.0)
        itemAppearance.selected.titleTextAttributes = [.foregroundColor: UIColor(red: 1.0, green: 0.3, blue: 0.0, alpha: 1.0)]
        
        appearance.stackedLayoutAppearance = itemAppearance
        appearance.inlineLayoutAppearance = itemAppearance
        appearance.compactInlineLayoutAppearance = itemAppearance
        
        UITabBar.appearance().standardAppearance = appearance
        if #available(iOS 15.0, *) {
            UITabBar.appearance().scrollEdgeAppearance = appearance
        }
    }
    
    var body: some View {
        TabView(selection: $selectedTab) {
            HomeView()
                .tabItem {
                    Image(systemName: "house.fill")
                    Text("Trang chủ")
                }
                .tag(0)
            
            ChatMemberView()
                .tabItem {
                    Image(systemName: "message.fill")
                    Text("Chat Member")
                }
                .tag(1)
            
            AdminView()
                .tabItem {
                    Image(systemName: "person.crop.circle.badge.exclamationmark")
                    Text("Admin")
                }
                .tag(2)
            
            SettingsView()
                .tabItem {
                    Image(systemName: "gearshape.fill")
                    Text("Cài đặt")
                }
                .tag(3)
        }
        .accentColor(accentColor)
        .background(bgColor.edgesIgnoringSafeArea(.all))
    }
}
