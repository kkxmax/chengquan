//
//  HotEstimationListViewController.h
//  chengxin
//
//  Created by common on 5/13/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HotEstimationListViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) IBOutlet UITableView *estimationTable;
@property (nonatomic, strong) NSMutableArray* aryEvalData;

-(IBAction)onBack:(id)sender;
@end
