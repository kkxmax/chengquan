//
//  HotViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/24/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface HotViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, NotificationDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tblHotView;
@property (weak, nonatomic) IBOutlet UILabel *messageNumberLabel;
@property (weak, nonatomic) IBOutlet UIView *viewBlank;
@property (weak, nonatomic) IBOutlet UIView *viewNoNetwork;

@end
