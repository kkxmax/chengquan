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

@interface HotDetailViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, UIScrollViewDelegate, UITextFieldDelegate>

@property (nonatomic, retain) HotObject *hotData;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;


@property (weak, nonatomic) IBOutlet UILabel *lblRead;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollInfoView;
@property (weak, nonatomic) IBOutlet UIButton *btnPersonal;
@property (weak, nonatomic) IBOutlet UILabel *personalSeparator;
@property (weak, nonatomic) IBOutlet UIButton *btnOffice;
@property (weak, nonatomic) IBOutlet UILabel *officeSeparator;
@property (weak, nonatomic) IBOutlet UIImageView *imgElect;
@property (weak, nonatomic) IBOutlet UILabel *lblShouChang;


@property (nonatomic, assign) CGFloat contentHeight;
@property (nonatomic, assign) int tblHeight;
@property (retain, nonatomic) UITableView *tblEvalView;
@property (retain, nonatomic) UITableView *tblOfficeView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *scrollDataViewHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tableContentHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *scrollinfoviewHeight;

@property (weak, nonatomic) IBOutlet UIPageControl *pageControl;

@property (weak, nonatomic) IBOutlet UIScrollView *scrollDataView;
@property (weak, nonatomic) IBOutlet UIView *tableContentView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *electTextTrailingConstraint;
@property (weak, nonatomic) IBOutlet UITextField *electContentTextField;
@property (weak, nonatomic) IBOutlet UIButton *electSendButton;

@property (weak, nonatomic) IBOutlet UILabel *electCountLabel;
@property (weak, nonatomic) IBOutlet UILabel *favouriteCountLabel;
@property (weak, nonatomic) IBOutlet UIButton *electButton;
@property (weak, nonatomic) IBOutlet UIButton *hotButton;
@property (weak, nonatomic) IBOutlet UIWebView *contentWebView;
@property (weak, nonatomic) IBOutlet UIView *blankView;

- (IBAction)onClickNavBackButton:(id)sender;
- (IBAction)onClickPersonalButton:(UIButton *)sender;
- (IBAction)onClickOfficeButton:(UIButton *)sender;
-(IBAction)onWriteEstimation:(id)sender;
-(IBAction)onEstimationList:(id)sender;
-(IBAction)onElect:(id)sender;
@end
