//
//  HotDetailViewController
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HotDetailSelectCell.h"
#import "HotObject.h"

@interface HotDetailViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, UIScrollViewDelegate>

@property (nonatomic, retain) HotObject *hotData;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;

@property (weak, nonatomic) IBOutlet UILabel *lblContent;
@property (weak, nonatomic) IBOutlet UILabel *lblRead;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollInfoView;
@property (weak, nonatomic) IBOutlet UIButton *btnPersonal;
@property (weak, nonatomic) IBOutlet UILabel *personalSeparator;
@property (weak, nonatomic) IBOutlet UIButton *btnOffice;
@property (weak, nonatomic) IBOutlet UILabel *officeSeparator;


@property (nonatomic, assign) CGFloat contentHeight;
@property (nonatomic, assign) int tblHeight;
@property (retain, nonatomic) UITableView *tblEvalView;
@property (retain, nonatomic) UITableView *tblOfficeView;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollPicView;
@property (weak, nonatomic) IBOutlet UIPageControl *pageControl;

- (IBAction)onClickNavBackButton:(id)sender;
- (IBAction)onClickPersonalButton:(UIButton *)sender;
- (IBAction)onClickOfficeButton:(UIButton *)sender;
@end
