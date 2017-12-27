//
//  EvalAwakenViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/4/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvalAwakenViewController : UIViewController<UITableViewDelegate, UITableViewDataSource, UIScrollViewDelegate>
@property (weak, nonatomic) IBOutlet UIButton *btnFront;
@property (weak, nonatomic) IBOutlet UILabel *sepFront;
@property (weak, nonatomic) IBOutlet UIButton *btnBack;
@property (weak, nonatomic) IBOutlet UILabel *sepBack;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollContentView;

@property (retain, nonatomic) UITableView *tblFrontView;
@property (retain, nonatomic) UITableView *tblBackView;

@property (weak, nonatomic) IBOutlet UILabel *frontMarkLabel;
@property (weak, nonatomic) IBOutlet UILabel *backMarkLabel;

- (IBAction)onClickFrontButton:(id)sender;
- (IBAction)onClickBackButton:(id)sender;

@property (nonatomic, assign) int selectType;
@property (nonatomic) BOOL isMineEvaluate;
@end
