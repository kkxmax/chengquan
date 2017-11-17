//
//  EvaluateViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvaluateViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UIButton *btnPersonal;
@property (weak, nonatomic) IBOutlet UILabel *imgPersonalLine;

@property (weak, nonatomic) IBOutlet UIButton *btnOffice;
@property (weak, nonatomic) IBOutlet UILabel *imgOfficeLine;
@property (weak, nonatomic) IBOutlet UITableView *tblView;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UILabel *messageNumberLabel;

@property (nonatomic, assign) BOOL bPersonal;

- (IBAction)onClickPersonalButton:(UIButton *)sender;
- (IBAction)onClickOfficeButton:(UIButton *)sender;
- (IBAction)onClickWriteEvaluation:(id)sender;

@end
