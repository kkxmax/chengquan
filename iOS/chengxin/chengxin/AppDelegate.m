//
//  AppDelegate.m
//  chengxin
//
//  Created by seniorcoder on 10/23/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "AppDelegate.h"
#import "MainViewController.h"
#import "LoginViewController.h"
#import "ForgotPasswordViewController.h"
#import "SignupSuccessViewController.h"
#import "FavouriteViewController.h"
#import "FavouritesTableViewController.h"
#import "MineViewController.h"
#import "LoginViewController.h"
#import "Global.h"

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    UINavigationController *navVC = [[UINavigationController alloc] init];
    // Override point for customization after application launch.
    LoginViewController *loginVC;
    if(IS_IPHONE_5_OR_LESS)
    {
        loginVC = [[LoginViewController alloc] initWithNibName:@"LoginViewController_iphone4" bundle:nil];
    }else
    {
        loginVC = [[LoginViewController alloc] initWithNibName:@"LoginViewController" bundle:nil];
    }
    
    
    //MainViewController *mainVC = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
    [navVC pushViewController:loginVC animated:NO];
    self.window.rootViewController = navVC;
    [self.window addSubview:navVC.view];
    [self.window makeKeyAndVisible];
    return YES;
}


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
