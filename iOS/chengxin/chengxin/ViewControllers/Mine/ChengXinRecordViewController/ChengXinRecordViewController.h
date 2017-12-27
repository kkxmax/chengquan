//
//  ChengXinRecordViewController.h
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChengXinDegreeView.h"

@interface ChengXinRecordViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, weak) IBOutlet UITableView* recordTableView;
@property (nonatomic, retain) IBOutlet ChengXinDegreeView* percentView;
-(IBAction)onBack:(id)sender;
-(IBAction)onRule:(id)sender;
@end
