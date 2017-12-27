//
//  MyJiuCuoViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyJiuCuoViewController : UIViewController <UIScrollViewDelegate, UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UIScrollView *scrollContentView;
@property (weak, nonatomic) IBOutlet UIButton *btnDetail;
@property (weak, nonatomic) IBOutlet UIButton *btnSuccess;
@property (weak, nonatomic) IBOutlet UIButton *btnFail;
@property (weak, nonatomic) IBOutlet UILabel *sepDetail;
@property (weak, nonatomic) IBOutlet UILabel *sepSuccess;
@property (weak, nonatomic) IBOutlet UILabel *sepFail;

@property (retain, nonatomic) UITableView *tblDetailView;
@property (retain, nonatomic) UITableView *tblSuccessView;
@property (retain, nonatomic) UITableView *tblFailView;

- (IBAction)onClickDetailButton:(id)sender;
- (IBAction)onClickSuccessButton:(id)sender;
- (IBAction)onClickFailButton:(id)sender;

@property (nonatomic, assign) int selectType;

@end
