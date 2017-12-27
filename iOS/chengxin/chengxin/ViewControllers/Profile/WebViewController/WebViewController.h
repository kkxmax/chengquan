//
//  WebViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/17/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WebViewController : UIViewController<UIWebViewDelegate>

@property (nonatomic, strong) IBOutlet UISearchBar *searchBar;
@property (nonatomic, strong) IBOutlet UIWebView *webView;
@property (nonatomic, strong) NSString *webUrl;

@end
