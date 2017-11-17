//
//  HomeChoiceBusinessTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 10/30/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeChoiceBusinessTableViewCell : UITableViewCell
{
}
@property (nonatomic, strong) UITableView *contentTableView;
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UIView *viewContent;
@property (nonatomic, strong) IBOutlet UIButton *extendButton;
@property (weak, nonatomic) IBOutlet UIButton *btnContent;
@end
