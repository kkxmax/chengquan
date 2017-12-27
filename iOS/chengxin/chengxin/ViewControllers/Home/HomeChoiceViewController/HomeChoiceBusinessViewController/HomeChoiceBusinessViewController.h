//
//  HomeChoiceBusinessViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChoiceCategoryViewController.h"

@protocol HomeChoiceBusinessViewDelegate<NSObject>
@optional

- (void)hideChoiceBussinessView:(NSDictionary *)categoryDic;
@end

@interface HomeChoiceBusinessViewController : UIViewController<UITableViewDataSource, UITableViewDelegate>
{
    id<HomeChoiceBusinessViewDelegate> delegate;
}
@property (nonatomic, retain) id<HomeChoiceBusinessViewDelegate> delegate;
@property (nonatomic, strong) IBOutlet UITableView *businessTableView;
@property (nonatomic, assign) NSInteger mChoice;
@property (nonatomic, assign) BOOL isSingleSelectionMode;
@property (nonatomic, strong) IBOutlet UILabel *allSelectLabel;
@property (nonatomic, strong) IBOutlet UILabel *navTitleLabel;
@property (nonatomic, strong) IBOutlet UIButton *cancelButton;
@property (nonatomic) BOOL isCancelButton;
@property (nonatomic, weak) IBOutlet UIView *limitView;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *limitViewHeight;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *buttonViewHeight;
@property (nonatomic, weak) IBOutlet NSLayoutConstraint *titleViewHeight;
@property (nonatomic, weak) IBOutlet UIView *buttonView;
-(IBAction)onClickCancelBtn:(id)sender;
- (IBAction)selectAction:(id)sender;
@end
