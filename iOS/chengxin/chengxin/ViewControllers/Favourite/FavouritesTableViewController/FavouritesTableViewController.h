//
//  FavouritesTableViewController.h
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MGSwipeTableCell.h"

@interface FavouritesTableViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, MGSwipeTableCellDelegate>

@property (nonatomic, retain) IBOutlet UITableView* tblInterView;
@property (nonatomic, assign) NSInteger selectType;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
-(IBAction)onBack:(id)sender;

@end
