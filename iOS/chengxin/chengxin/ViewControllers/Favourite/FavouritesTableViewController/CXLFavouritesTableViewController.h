//
//  FavouritesTableViewController.h
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MGSwipeTableCell.h"

@interface CXLFavouritesTableViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, MGSwipeTableCellDelegate>

@property (nonatomic, retain) IBOutlet UITableView* tblInterView;
@property (nonatomic, assign) NSInteger selectType;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;

@property (weak, nonatomic) IBOutlet UIButton *btnPersonal;
@property (weak, nonatomic) IBOutlet UILabel *imgPersonalLine;

@property (weak, nonatomic) IBOutlet UIButton *btnOffice;
@property (weak, nonatomic) IBOutlet UILabel *imgOfficeLine;

@property (weak, nonatomic) IBOutlet UIView *blankView;

-(IBAction)onBack:(id)sender;
- (IBAction)onClickPersonalButton:(UIButton *)sender;
- (IBAction)onClickOfficeButton:(UIButton *)sender;
@end
