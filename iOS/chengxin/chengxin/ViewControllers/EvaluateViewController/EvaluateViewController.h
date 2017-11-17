//
//  EvaluateViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
@interface EvaluateViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, NotificationDelegate>
@property (weak, nonatomic) IBOutlet UIButton *btnPersonal;
@property (weak, nonatomic) IBOutlet UILabel *imgPersonalLine;

@property (weak, nonatomic) IBOutlet UIButton *btnOffice;
@property (weak, nonatomic) IBOutlet UILabel *imgOfficeLine;
@property (weak, nonatomic) IBOutlet UITableView *tblView;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UILabel *messageNumberLabel;
@property (weak, nonatomic) IBOutlet UIView *viewDetailNavBar;
@property (weak, nonatomic) IBOutlet UIView *viewGeneralNavBar;
@property (weak, nonatomic) IBOutlet UIView *viewBlank;
@property (weak, nonatomic) IBOutlet UIView *viewNoNetwork;

@property (nonatomic, assign) BOOL bIsDetail;
@property (nonatomic, assign) BOOL bPersonal;

@property (weak, nonatomic) IBOutlet UIView *homeChoiceBackgroundView;
@property (weak, nonatomic) IBOutlet UIView *homeChoiceTransView;
@property (weak, nonatomic) IBOutlet UIView *homeChoiceView;

- (IBAction)onClickPersonalButton:(UIButton *)sender;
- (IBAction)onClickOfficeButton:(UIButton *)sender;
- (IBAction)onClickWriteEvaluation:(id)sender;

@end
