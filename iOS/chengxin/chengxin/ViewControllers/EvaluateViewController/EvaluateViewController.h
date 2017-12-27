//
//  EvaluateViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

typedef enum {
    em_Estimation,
    em_MyEstimation,
    em_EstimationToMe
} Estimation_Type;

@interface EvaluateViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, NotificationDelegate, UIAlertViewDelegate>
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

@property (weak, nonatomic) IBOutlet UIButton *editButton;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;

@property (nonatomic, strong) IBOutletCollection(UILabel) NSArray *sortNameLabel;
@property (nonatomic, strong) IBOutletCollection(UIImageView) NSArray *sortCheckImage;

@property (weak, nonatomic) IBOutlet UIView *sortBackView;
@property (weak, nonatomic) IBOutlet UIView *sortEmptyView;
@property (weak, nonatomic) IBOutlet UIView *sortView;

@property Estimation_Type estimationType;

- (IBAction)onClickPersonalButton:(UIButton *)sender;
- (IBAction)onClickOfficeButton:(UIButton *)sender;
- (IBAction)onClickWriteEvaluation:(id)sender;
- (IBAction)onEmpty:(id)sender;
- (IBAction)onSortSelection:(id)sender;
@end
