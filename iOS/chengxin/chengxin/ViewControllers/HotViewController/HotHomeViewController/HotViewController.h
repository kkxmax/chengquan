//
//  HotViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/24/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HotViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tblHotView;
@property (weak, nonatomic) IBOutlet UILabel *messageNumberLabel;

@end
