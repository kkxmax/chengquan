//
//  HotDetailViewController
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HotDetailSelectCell.h"

@interface HotDetailViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UILabel *lblContent;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollInfoView;
@property (weak, nonatomic) IBOutlet UIButton *btnPersonal;
@property (weak, nonatomic) IBOutlet UILabel *personalSeparator;
@property (weak, nonatomic) IBOutlet UIButton *btnOffice;
@property (weak, nonatomic) IBOutlet UILabel *officeSeparator;


@property (nonatomic, assign) CGFloat contentHeight;
@property (nonatomic, assign) int tblHeight;
@property (retain, nonatomic) UITableView *tblPersonalView;
@property (retain, nonatomic) UITableView *tblOfficeView;

- (IBAction)onClickNavBackButton:(id)sender;
- (IBAction)onClickPersonalButton:(UIButton *)sender;
- (IBAction)onClickOfficeButton:(UIButton *)sender;
@end
